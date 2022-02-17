import cv2
import mediapipe as mp
import math
import numpy as np
from keras.models import load_model
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
import base64
import io
from PIL import Image, ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True
from django.shortcuts import render
from .serializers import GesturesSerializer

# Create your views here.


# mp_drawing=mp.solutions.drawing_utils
# mp_hands=mp.solutions.hands

cap=cv2.VideoCapture(0)
# global arr
# arr=np.array([])
model=load_model(r"D:\CtoD\PycharmProjects\Zinara\zinzaraServer\gestures\model_test.h5")


@csrf_exempt
def find_distance(a1,a2,b1,b2):
    n1=abs(a1-b1)
    n2=abs(a2-b2)
    return round(math.sqrt((n1**2)+(n2**2)),4)


@csrf_exempt
def find_gradient(a1,a2,b1,b2):
    n1=b1-a1
    n2=b2-a2
    if n1==0:
        return 0
    else:
        return round(n2/n1*-1,4)


@csrf_exempt
def gestures(request):
    data = JSONParser().parse(request)
    img = data["img"]
    print("aaa")
    # img = img + "=" * (4 - len(img) % 4)
    # img = base64.b64decode(img)
    # print("bbb")
    # dataBytesIO = io.BytesIO(img)
    # print("ccc")
    # image = Image.open(dataBytesIO)
    # print("ddd")
    # image = cv2.cvtColor(np.array(img))
    # print("eee")


    # img = img + "=" * (4 - len(img) % 4)
    # print("bbb")
    # img = base64.b64decode(img)
    # print("ccc")
    # dataBytesIO = io.BytesIO(img)
    # print("ddd")
    # image = Image.open(dataBytesIO)
    # print("eee")
    # image = cv2.cvtColor(np.array(image))
    # print("fff")


    img = img + "=" * (4 - len(img) % 4)

    imgData = base64.b64decode(str(img))
    print("aaa")
    img = Image.open(io.BytesIO(imgData))
    print("bbb")
    image = cv2.cvtColor(np.array(img), cv2.COLOR_BGR2RGB)
    print("ccc")

    mp_drawing = mp.solutions.drawing_utils
    mp_hands = mp.solutions.hands
    with mp_hands.Hands(max_num_hands=1, min_detection_confidence=0.5, min_tracking_confidence=0.7) as hands:
        #image = cv2.imread("50.jpg", cv2.IMREAD_COLOR)

        #image3 = request.GET.get("img")

        #print(image3)
        #image = np.ndarray(image3, dtype=np.uint8)
        print("success")
        print(type(image))
        print(image.dtype)
        print(image.shape)
        image = cv2.cvtColor(image, cv2.COLOR_GRAY2BGR)
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image = cv2.flip(image, 1)
        print(image.shape)
        results = hands.process(image)
        image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
        arr = np.array([])

        print("000")

        if results.multi_hand_landmarks:
            print("zzz")
            for hand_landmarks in results.multi_hand_landmarks:
                mp_drawing.draw_landmarks(image, hand_landmarks, mp_hands.HAND_CONNECTIONS)

            print("111")

            for i in range(1, 20):
                if i == 4:
                    arr = np.append(arr, (float(
                        find_distance(int(hand_landmarks.landmark[1].x * 255), int(hand_landmarks.landmark[1].y * 255),
                                      int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255)))))
                elif i == 8:
                    arr = np.append(arr, (float(
                        find_distance(int(hand_landmarks.landmark[5].x * 255), int(hand_landmarks.landmark[5].y * 255),
                                      int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255)))))
                elif i == 12:
                    arr = np.append(arr, (float(
                        find_distance(int(hand_landmarks.landmark[9].x * 255), int(hand_landmarks.landmark[9].y * 255),
                                      int(hand_landmarks.landmark[12].x * 255), int(hand_landmarks.landmark[12].y * 255)))))
                elif i == 16:
                    arr = np.append(arr, (float(
                        find_distance(int(hand_landmarks.landmark[13].x * 255), int(hand_landmarks.landmark[13].y * 255),
                                      int(hand_landmarks.landmark[16].x * 255), int(hand_landmarks.landmark[16].y * 255)))))
                else:
                    arr = np.append(arr, (float(
                        find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                      int(hand_landmarks.landmark[i + 1].x * 255),
                                      int(hand_landmarks.landmark[i + 1].y * 255)))))
            arr = np.append(arr, (
                float(find_distance(int(hand_landmarks.landmark[17].x * 255), int(hand_landmarks.landmark[17].y * 255),
                                    int(hand_landmarks.landmark[20].x * 255), int(hand_landmarks.landmark[20].y * 255)))))

            for i in range(1, 20):
                if i == 4:
                    arr = np.append(arr, (float(
                        find_gradient(int(hand_landmarks.landmark[1].x * 255), int(hand_landmarks.landmark[1].y * 255),
                                      int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255)))))
                elif i == 8:
                    arr = np.append(arr, (float(
                        find_gradient(int(hand_landmarks.landmark[5].x * 255), int(hand_landmarks.landmark[5].y * 255),
                                      int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255)))))
                elif i == 12:
                    arr = np.append(arr, (float(
                        find_gradient(int(hand_landmarks.landmark[9].x * 255), int(hand_landmarks.landmark[9].y * 255),
                                      int(hand_landmarks.landmark[12].x * 255), int(hand_landmarks.landmark[12].y * 255)))))
                elif i == 16:
                    arr = np.append(arr, (float(
                        find_gradient(int(hand_landmarks.landmark[13].x * 255), int(hand_landmarks.landmark[13].y * 255),
                                      int(hand_landmarks.landmark[16].x * 255), int(hand_landmarks.landmark[16].y * 255)))))
                else:
                    arr = np.append(arr, (float(
                        find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                      int(hand_landmarks.landmark[i + 1].x * 255),
                                      int(hand_landmarks.landmark[i + 1].y * 255)))))
            arr = np.append(arr, (
                float(find_gradient(int(hand_landmarks.landmark[17].x * 255), int(hand_landmarks.landmark[17].y * 255),
                                    int(hand_landmarks.landmark[20].x * 255), int(hand_landmarks.landmark[20].y * 255)))))

            print("222")

            arr = arr.reshape(-1, 40)

            tmp = model.predict(arr)
            predicted = np.argmax(tmp, axis=1)
            print(predicted)
            arr = np.array([])
            print("333")
            return HttpResponse(status=580)

        else:
            print("vvv")
            return HttpResponse(status=590)



