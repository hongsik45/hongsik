#
import json
import os
import requests
import math
from PIL import Image
import subprocess
from os import listdir
from os.path import isfile, join
import sys
import cv2
import numpy as np
from scipy import misc

#s = input('운동종류입력:')
s = sys.argv[1] #운동 매개변수 보내기
f = sys.argv[2] #파일폴더 이름 보내기

def Angle(a1,a2,b1,b2,c1,c2): #세변의 길이를 알 때 끼인각 구하기
    d1=1
    d2=1
    d3=1
    result = 180

    if a1 != 0.0 and a2 != 0.0 and b1 != 0.0 and b2 != 0.0:
        d1 = math.sqrt((a1 - b1) * (a1 - b1) + (a2 - b2) * (a2 - b2))
    if b1 != 0.0 and b2 != 0.0 and c1 != 0.0 and c2 != 0.0:
        d2 = math.sqrt((b1 - c1) * (b1 - c1) + (b2 - c2) * (b2 - c2))
    if a1 != 0.0 and a2 != 0.0 and c1 != 0.0 and c2 != 0.0:
        d3 = math.sqrt((a1 - c1) * (a1 - c1) + (a2 - c2) * (a2 - c2))

    if d1 != 1 and d2 != 1 and d3 != 1 and d1!=0 and d2!=0:
        cosc1 = (d1*d1 + d2*d2 - d3*d3) /(2*d1*d2)
        result = math.acos(cosc1) * (180 / 3.14)
    return result

def distance(x1,y1,x2,y2):
    result=math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))
    return result

def SquartChk(path3,count): # 스쿼트 판정 함수
    flag = 0
    ret=0
    for file in path3:
        if file.endswith(".json"):
            with open(path3 + "/" + file, encoding="utf-8") as data_file:
                # json파일 불러오기
                data = json.load(data_file)
                cnt=0
                m=1e9
                r=0

                if (data["people"]):  # 사람이 인식됬을경우
                    for i in data["people"]:
                        a = (i["pose_keypoints_2d"][27]+i["pose_keypoints_2d"][36])/2
                        b = (i["pose_keypoints_2d"][28]+i["pose_keypoints_2d"][37])/2
                        n = distance(128, 200, a, b)

                        if m > n:
                            m = n
                            r = cnt

                        cnt = cnt + 1

                    RHX = data["people"][r]["pose_keypoints_2d"][24]
                    RHY = data["people"][r]["pose_keypoints_2d"][25]  # 오른쪽엉덩이Y
                    RKX = data["people"][r]["pose_keypoints_2d"][27]
                    RKY = data["people"][r]["pose_keypoints_2d"][28]  # 오른쪽무릎Y
                    RAX = data["people"][r]["pose_keypoints_2d"][30]
                    RAY = data["people"][r]["pose_keypoints_2d"][31]  # 오른쪽발목Y

                    LHX = data["people"][r]["pose_keypoints_2d"][33]
                    LHY = data["people"][r]["pose_keypoints_2d"][34]  # 왼쪽엉덩이Y
                    LKX = data["people"][r]["pose_keypoints_2d"][36]
                    LKY = data["people"][r]["pose_keypoints_2d"][37]  # 왼쪽무릎Y
                    LAX = data["people"][r]["pose_keypoints_2d"][39]
                    LAY = data["people"][r]["pose_keypoints_2d"][40]  # 왼쪽발목Y

                    result1=Angle(RHX,RHY,RKX,RKY,RAX,RAY) # 오른쪽 다리 접는 각도
                    result2=Angle(LHX,LHY,LKX,LKY,LAX,LAY) # 왼쪽 다리 접는 각도

                    #print("")
                    #print(file)
                    #print(result1)
                    #print(result2)

                    data_file.close()
                    #os.remove("/home/ubuntu/openpose/json/"+file)  # json파일 삭제

                    # 스쿼트 판정->양쪽 다리각도가 140보다 작으면 카운트업
                    if result1 < 140 or result2 < 140:
                        if flag==0:
                            #print(file)
                            flag=1
                            count = count + 1
                    else :
                        flag=0
                else:  # 사람이 인식되지 않았을 경우 실패
                    count = count #카운트 유지
                    data_file.close()
                    os.remove("/home/ubuntu/openpose/json/"+file)  # json파일 삭제
    if count>=13:
        ret=1
    #print(count)
    return ret

def Stretching(file_list):
    ret=0
    flag1 = 1 #중간에 자세유지 못하면 카운트 초기화 플래그
    flag2 = 1
    flag3 = 1
    cnt1=0 #왼쪽머리 기울이기 스트레칭 연속사진 수
    cnt2=0 #오른쪽머리 기울이기 스트레칭 연속사진 수
    cnt3=0 #위로머리 기울이기 스트레칭 연속사진 수
    for file in file_list:
        if file.endswith(".json"):
            with open(path3 + "/" + file, encoding="utf-8") as data_file:
                # json파일 불러오기
                data = json.load(data_file)
                cnt=0
                m=1e9
                r=0
                if (data["people"]):  # 사람이 인식됬을경우
                    for i in data["people"]:
                        a = i["pose_keypoints_2d"][3]
                        b = i["pose_keypoints_2d"][4]
                        n = distance(128, 128, a, b)
                        if m > n:
                            m = n
                            r = cnt

                        cnt = cnt + 1

                    # 코 ,오른쪽,왼쪽 귀, 목, 오른쪽,왼쪽 어깨 좌표
                    REX = data["people"][r]["pose_keypoints_2d"][42]
                    REY = data["people"][r]["pose_keypoints_2d"][43]
                    LEX = data["people"][r]["pose_keypoints_2d"][45]
                    LEY = data["people"][r]["pose_keypoints_2d"][46]
                    NeckX = data["people"][r]["pose_keypoints_2d"][3]
                    NeckY = data["people"][r]["pose_keypoints_2d"][4]
                    RSX = data["people"][r]["pose_keypoints_2d"][6]
                    RSY = data["people"][r]["pose_keypoints_2d"][7]
                    LSX = data["people"][r]["pose_keypoints_2d"][15]
                    LSY = data["people"][r]["pose_keypoints_2d"][16]
                    RWY = data["people"][r]["pose_keypoints_2d"][13]
                    LWY = data["people"][r]["pose_keypoints_2d"][22]
                    RWX = data["people"][r]["pose_keypoints_2d"][12]
                    LWX = data["people"][r]["pose_keypoints_2d"][21]
                    NoseY = data["people"][r]["pose_keypoints_2d"][1]

                    result1=Angle(REX,REY,LEX,LEY,LEX,REY)

                    data_file.close()
                    os.remove("/home/ubuntu/openpose/json/"+file)  # json파일 삭제

                    # 스트레칭 판정->귀와 어깨의 사잇각이 70도 미만인 채 10초 유지하면 그 단계통과 왼쪽,어깨 위 10초씩 유지하면 통과 10초기준->30장 사진 판정유지
                    #               위로 머리 기울이기 스트레칭 기준->어깨 사이길이보다 손목사이 길이가 작아야한다.&&두 손목 모은 채 어깨 근처에 있어야한다.
                    if result1<70 and REY>LEY:     #오른쪽으로 고개 기울이기
                        if flag1:
                            #print("a"+file)
                            cnt1 = cnt1 + 1
                        else:
                            if cnt1<3:
                                #print("b"+file)
                                cnt1=1
                                flag1=1
                            else:
                                cnt1 = cnt1 + 1
                                flag1 = 1
                    else :
                        flag1=0
                        #if cnt1<24:
                        #    cnt1=0

                    if result1<70 and LEY>REY:
                        if flag2:
                            cnt2 = cnt2 + 1
                            #print("c"+file)
                        else:
                            if cnt2<3:
                                cnt2=1
                                flag2=1
                                #print("d"+file)
                            else:
                                cnt2=cnt2+1
                                flag2=1
                    else :
                        flag2=0
                        #if cnt2<24:
                        #    cnt2=0

                    #print("")
                    #print(file)
                    #print(RWY)
                    #print(RSY)
                    #print(LWY)
                    #print(LSY)
                    #print(LSX)
                    #print(RSX)
                    #print(LWX)
                    #print(RWX)
                    #print(NoseY)

                    if RWY!=0 and RSY!=0 and LWY!=0 and LSY!=0 and LSX!=0 and RSX!=0 and LWX!=0 and RWX!=0:
                        if ((math.fabs(RWY-RSY)<20 and math.fabs(LWY-LSY)<20)or(RWY<RSY and LWY<LSY)) and ((LSX - RSX) > (LWX - RWX)):
                            if flag3:
                                cnt3 = cnt3 + 1
                                #print(file)
                            else:
                                if cnt3 < 3:
                                    cnt3 = 1
                                    flag3 = 1
                                    #print(file)
                                else:
                                    cnt3 = cnt3+1
                                    flag3=1
                                    #print(file)
                        else:
                            flag3 = 0
                                #if cnt3 < 24:
                                #    cnt3 = 0
                                    #print(file + '3')

                else:  # 사람이 인식되지 않았을 경우 실패
                    data_file.close()
                    os.remove("/home/ubuntu/openpose/json/"+file)  # json파일 삭제
        if cnt1>=20 and cnt2>=20 and cnt3>=20:
            ret=1
    #print(cnt1)
    #print(cnt2)
    #print(cnt3)
    return ret

def Stretching2(file_list, count): #팔 W자 만들면서 등 뒤로 당기기
    flag1 = 0
    ret=0
    for file in file_list:
        if file.endswith(".json"):
            with open(path3 + "/" + file, encoding="utf-8") as data_file:
                # json파일 불러오기
                data = json.load(data_file)
                cnt=0
                m=1e9
                r=0

                if (data["people"]):  # 사람이 인식됬을경우
                    for i in data["people"]:
                        a = i["pose_keypoints_2d"][0]
                        b = i["pose_keypoints_2d"][1]
                        n = distance(128, 128, a, b)
                        if m > n:
                            m = n
                            r = cnt
                        cnt = cnt + 1
                        #print("")
                        #print(file)
                        #print(r)
                    # 왼쪽 무릎과 발목 Y좌표 차이
                    RSX = data["people"][r]["pose_keypoints_2d"][6]
                    RSY = data["people"][r]["pose_keypoints_2d"][7]
                    REX = data["people"][r]["pose_keypoints_2d"][9]
                    REY = data["people"][r]["pose_keypoints_2d"][10]
                    RWX = data["people"][r]["pose_keypoints_2d"][12]
                    RWY = data["people"][r]["pose_keypoints_2d"][13]
                    LSX = data["people"][r]["pose_keypoints_2d"][15]
                    LSY = data["people"][r]["pose_keypoints_2d"][16]
                    LEX = data["people"][r]["pose_keypoints_2d"][18]
                    LEY = data["people"][r]["pose_keypoints_2d"][19]
                    LWX = data["people"][r]["pose_keypoints_2d"][21]
                    LWY = data["people"][r]["pose_keypoints_2d"][22]
                    #print("")
                    #print(file)
                    #print(LSX)
                    #print(LSY)
                    #print(LEX)
                    #print(LEY)
                    #print(LWX)
                    #print(LWY)
                    result1=Angle(RSX,RSY,REX,REY,RWX,RWY)# 오른쪽 팔 접는각도
                    result2=Angle(LSX,LSY,LEX,LEY,LWX,LWY)# 왼쪽 팔 접는각도

                    data_file.close()
                    os.remove("/home/ubuntu/openpose/json/"+file)  # json파일 삭제
                    #print(result1)
                    #print(result2)
                    # 스트레칭2 판정->양쪽 팔 접는각도 120 미만 && 어깨가 팔꿈치보다 위에 있어야 된다.
                    if result1<120 and result2<120 and RSY<REY and LSY<LEY:
                        if flag1 == 0:
                            #print(file)
                            flag1 = 1
                            count = count + 1
                    else:
                        flag1 = 0

                else:  # 사람이 인식되지 않았을 경우 실패
                    count = count
                    data_file.close()
                    os.remove("/home/ubuntu/openpose/json/"+file)  # json파일 삭제
    if count>=15:
        ret=1
    #print(count)
    return ret

def SideLunge(file_list, count):
    ret=0
    flag = 0
    for file in file_list:
        if file.endswith(".json"):
            with open(path3 + "/" + file, encoding="utf-8") as data_file:
                # json파일 불러오기
                data = json.load(data_file)
                cnt=0
                m=1e9
                r=0

                if (data["people"]):  # 사람이 인식됬을경우
                    for i in data["people"]:
                        a = (i["pose_keypoints_2d"][24]+ i["pose_keypoints_2d"][33])/2
                        b = (i["pose_keypoints_2d"][25]+ i["pose_keypoints_2d"][34])/2
                        n = distance(128, 200, a, b)
                        if m > n:
                            m = n
                            r = cnt
                        cnt = cnt + 1
                    # 왼쪽 무릎과 발목 Y좌표 차이
                    RHX = data["people"][r]["pose_keypoints_2d"][24]
                    RHY = data["people"][r]["pose_keypoints_2d"][25]  #오른쪽엉덩이Y
                    RKX = data["people"][r]["pose_keypoints_2d"][27]
                    RKY = data["people"][r]["pose_keypoints_2d"][28]  #오른쪽무릎Y
                    RAX = data["people"][r]["pose_keypoints_2d"][30]
                    RAY = data["people"][r]["pose_keypoints_2d"][31]  #오른쪽발목Y

                    LHX = data["people"][r]["pose_keypoints_2d"][33]
                    LHY = data["people"][r]["pose_keypoints_2d"][34]  #왼쪽엉덩이Y
                    LKX = data["people"][r]["pose_keypoints_2d"][36]
                    LKY = data["people"][r]["pose_keypoints_2d"][37]  #왼쪽무릎Y
                    LAX = data["people"][r]["pose_keypoints_2d"][39]
                    LAY = data["people"][r]["pose_keypoints_2d"][40]  #왼쪽발목Y

                    result1=Angle(RHX,RHY,RKX,RKY,RAX,RAY)#오른쪽 발 접는각도
                    result2=Angle(LHX,LHY,LKX,LKY,LAX,LAY)#왼쪽 발 접는각도

                    #print("")
                    #print(file)
                    #print(result1)
                    #print(result2)

                    data_file.close()
                    os.remove("/home/ubuntu/openpose/json/"+file)  # json파일 삭제

                    # 사이드 런지 판정->한쪽 다리의 각도는 160도 이상이여야 하고 다른쪽 다리의 각도는 120도 미만으로 접혀져야 한다.
                    if (result1 < 120 and result2 > 150)or(result1>150 and result2<120):
                        if flag == 0:
                            #print(file)
                            flag = 1
                            count = count + 1
                    else:
                        flag = 0
                else:  # 사람이 인식되지 않았을 경우 실패
                    count = count
                    data_file.close()
                    os.remove("/home/ubuntu/openpose/json/"+file)  # json파일 삭제
    if count>=20:
        ret=1
    #print(count)
    return ret

count = 0
# openpose 실행 코드->해당 디렉0토리 이미지 파일 오픈포즈 실행
#path2 = "cd /home/ubuntu/openpose && /home/ubuntu/openpose/build/example/openpose/openpose.bin --image_dir /home/ubuntu/openpose/test --write_json /home/ubuntu/openpose/json --net_resolution 256x256"
# 명령어 실행
./var/www/html/path2
# json파일 불러오기
path3 = "/home/ubuntu/openpose/json"
#file_list = os.listdir(path3)
if s=='0':
    ret1=SquartChk(path3, count)
    url = "http://127.0.0.1/index0.php"
    r = requests.get(url, params={'value': ret1})
    #print(r.text)
    #print(s)
    #print(f)
    #print(ret1)
elif s=='1':
    ret2=Stretching(path3)
    url = "http://127.0.0.1/index0.php"
    r = requests.get(url, params={'value': ret2})
    #print(ret2)
elif s=='2':
    ret3=Stretching2(path3)
    url = "http://127.0.0.1/index0.php"
    r = requests.get(url, params={'value': ret3})
    #print(ret3)
elif s=='3':
    ret4=SideLunge(path3)
    url = "http://127.0.0.1/index0.php"
    r = requests.get(url, params={'value': ret4})
    #print(ret4)
