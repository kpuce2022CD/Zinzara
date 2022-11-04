import cv2
import mediapipe as mp
import math
import os

mp_drawing = mp.solutions.drawing_utils
mp_hands = mp.solutions.hands

os.makedirs("imageData", exist_ok=True)
for i in range(10):
    os.makedirs("imageData/"+str(i), exist_ok=True)

cap = cv2.VideoCapture(0)
cnt = 1
n = 2   # 0~9까지 설정
f = open("data"+str(n)+".csv", "a")


def find_distance(a1, a2, b1, b2):
    n1 = abs(a1 - b1)
    n2 = abs(a2 - b2)
    return round(math.sqrt((n1**2) + (n2**2)), 4)


def find_gradient(a1, a2, b1, b2):
    n1 = b1-a1
    n2 = b2-a2
    if n1 == 0:
        return 0
    else:
        return round(n2/n1*-1, 4)


def find_angle(a1, a2, b1, b2):
    n1 = b1 - a1
    n2 = b2 - a2


with mp_hands.Hands(max_num_hands=1, min_detection_confidence=0.5, min_tracking_confidence=0.7) as hands:
    while cap.isOpened():
        success, image = cap.read()
        if not success:
            continue

        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image = cv2.flip(image, 1)
        results = hands.process(image)
        image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                mp_drawing.draw_landmarks(image, hand_landmarks, mp_hands.HAND_CONNECTIONS)

        cv2.imshow('image', image)

        if cv2.waitKey(1) == 97:   # a키 입력
            for i in range(21):
                f.write(str(int(hand_landmarks.landmark[i].x * 255)) + "," + str(int(hand_landmarks.landmark[i].y * 255)) + ",")

            for i in range(1, 20):
                if i == 4:
                    f.write(str(find_distance(int(hand_landmarks.landmark[1].x * 255), int(hand_landmarks.landmark[1].y * 255),
                                              int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255))) + ",")
                elif i == 8:
                    f.write(str(find_distance(int(hand_landmarks.landmark[5].x * 255), int(hand_landmarks.landmark[5].y * 255),
                                              int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255))) + ",")
                elif i == 12:
                    f.write(str(find_distance(int(hand_landmarks.landmark[9].x * 255), int(hand_landmarks.landmark[9].y * 255),
                                              int(hand_landmarks.landmark[12].x * 255), int(hand_landmarks.landmark[12].y * 255))) + ",")
                elif i == 16:
                    f.write(str(find_distance(int(hand_landmarks.landmark[13].x * 255), int(hand_landmarks.landmark[13].y * 255),
                                              int(hand_landmarks.landmark[16].x * 255), int(hand_landmarks.landmark[16].y * 255))) + ",")
                else:
                    f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                              int(hand_landmarks.landmark[i + 1].x * 255), int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
            f.write(str(find_distance(int(hand_landmarks.landmark[17].x * 255), int(hand_landmarks.landmark[17].y * 255),
                                      int(hand_landmarks.landmark[20].x * 255), int(hand_landmarks.landmark[20].y * 255))) + ",")

            for i in range(1, 20):
                if i == 4:
                    f.write(str(find_gradient(int(hand_landmarks.landmark[1].x * 255), int(hand_landmarks.landmark[1].y * 255),
                                              int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255))) + ",")
                elif i == 8:
                    f.write(str(find_gradient(int(hand_landmarks.landmark[5].x * 255), int(hand_landmarks.landmark[5].y * 255),
                                              int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255))) + ",")
                elif i == 12:
                    f.write(str(find_gradient(int(hand_landmarks.landmark[9].x * 255), int(hand_landmarks.landmark[9].y * 255),
                                              int(hand_landmarks.landmark[12].x * 255), int(hand_landmarks.landmark[12].y * 255))) + ",")
                elif i == 16:
                    f.write(str(find_gradient(int(hand_landmarks.landmark[13].x * 255), int(hand_landmarks.landmark[13].y * 255),
                                              int(hand_landmarks.landmark[16].x * 255), int(hand_landmarks.landmark[16].y * 255))) + ",")
                else:
                    f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                              int(hand_landmarks.landmark[i + 1].x * 255), int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
            f.write(str(find_gradient(int(hand_landmarks.landmark[17].x * 255), int(hand_landmarks.landmark[17].y * 255),
                                      int(hand_landmarks.landmark[20].x * 255), int(hand_landmarks.landmark[20].y * 255))) + "," + str(n) + "\n")

            cv2.imwrite("imageData/"+str(n)+"/"+str(cnt)+".jpg", image)

            print(cnt)
            cnt += 1

        elif cv2.waitKey(1) == 27:   # ESC키 입력시 종료
            break


f.close()
cap.release()
