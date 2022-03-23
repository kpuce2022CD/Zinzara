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

    img = img + "=" * (4 - len(img) % 4)
    img = base64.b64decode(img)
    dataBytesIO = io.BytesIO(img)
    image = Image.open(dataBytesIO)

    image = cv2.cvtColor(np.array(image), cv2.IMREAD_COLOR)   # color
    #image = cv2.cvtColor(np.array(image), cv2.IMREAD_GRAYSCALE)   # grayscale

    mp_drawing = mp.solutions.drawing_utils
    mp_hands = mp.solutions.hands
    with mp_hands.Hands(max_num_hands=1, min_detection_confidence=0.3, min_tracking_confidence=0.3) as hands:
        #image = cv2.cvtColor(image, cv2.COLOR_GRAY2RGB)   # grayscale
        #image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)   # color
        # image = cv2.flip(image, 1)
        results = hands.process(image)
        arr = np.array([])

        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                mp_drawing.draw_landmarks(image, hand_landmarks, mp_hands.HAND_CONNECTIONS)

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

            light_control = float(find_distance(int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255),
                                                int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255)))

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

            arr = arr.reshape(-1, 40)

            tmp = model.predict(arr)
            predicted = np.argmax(tmp, axis=1)
            print(predicted)
            predicted = int(predicted)
            arr = np.array([])
            if predicted == 0:
                return HttpResponse(status=210)
            elif predicted == 1:
                return HttpResponse(status=211)
            elif predicted == 2:
                return HttpResponse(status=212)
            elif predicted == 3:
                return HttpResponse(status=213)
            elif predicted == 4:
                return HttpResponse(status=214)
            elif predicted == 5:
                return HttpResponse(status=215)
            elif predicted == 6:
                return HttpResponse(status=216)
            elif predicted == 7:
                return HttpResponse(status=217)
            elif predicted == 8:
                return HttpResponse(status=218)
            elif predicted == 9:   # 간격 리턴
                return HttpResponse("219-"+str(light_control))

        else:
            print("fail")
            return HttpResponse(status=220)



