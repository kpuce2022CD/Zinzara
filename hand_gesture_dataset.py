import cv2
import mediapipe as mp
import math
import csv

mp_drawing=mp.solutions.drawing_utils
mp_hands=mp.solutions.hands

cap=cv2.VideoCapture(0)


def find_distance(a1,a2,b1,b2):
    n1=abs(a1-b1)
    n2=abs(a2-b2)
    return math.sqrt((n1**2)+(n2**2))

def find_gradient(a1,a2,b1,b2):
    n1=b1-a1
    n2=(a2-b2)*-1
    if n1==0:
        return 0
    else:
        return n2/n1*-1


with mp_hands.Hands(max_num_hands=1, min_detection_confidence=0.5, min_tracking_confidence=0.5) as hands:

    while cap.isOpened():
        success, image=cap.read()
        if not success:
            continue

        image=cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image=cv2.flip(image, 1)
        results=hands.process(image)
        image=cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                finger1=int(hand_landmarks.landmark[4].x * 255)
                finger2=int(hand_landmarks.landmark[8].x * 255)
                dist=abs(finger1-finger2)
                #print(find_distance(int(hand_landmarks.landmark[4].x*255),int(hand_landmarks.landmark[4].y*255),
                #                    int(hand_landmarks.landmark[8].x*255),int(hand_landmarks.landmark[8].y*255)))

                print(find_gradient(int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255),
                                    int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255)))



                cv2.putText(
                    image, text='f1=%d f2=%d dist=%d ' % (finger1, finger2, dist), org=(10, 30),
                    fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1,
                    color=255, thickness=3)

                mp_drawing.draw_landmarks(image, hand_landmarks, mp_hands.HAND_CONNECTIONS)

        cv2.imshow('image',image)

        if cv2.waitKey(1)==48:   # 키보드 0 눌렀을때
            break


        if cv2.waitKey(1)==27:   # 키보드 ESC 눌렀을때 종료됨
            break

cap.release()