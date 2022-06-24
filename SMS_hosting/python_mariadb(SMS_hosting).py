import requests
import json
import mariadb
import sys
import time
import pandas as pd

send_url = "https://apis.aligo.in/send/"

try:
    conn = mariadb.connect(
        user="userid",
        password="pw",
        host="IP",
        port=PortNum,
        database="databaseName"
    )
except mariadb.Error as e:
    print("{}".format(e))
    sys.exit(1)

cur = conn.cursor()
cur.execute("select * from physical_rehabilitation")
result1 = cur.fetchall()

cur.execute("select * from language_rehabilitation")
result2 = cur.fetchall()

cur.execute("select * from members")
result3 = cur.fetchall()

number_d = {}
d = {}
clear_d = {}

# data = pd.DataFrame.from_records(res)
# print(data)

for record in result1:
    clear_d[record[3]] = []

for record in result2:
    if record[3] not in clear_d.keys():
        clear_d[record[3]] = []

for record in result3:
    if record[0] not in number_d.keys():
        number_d[record[0]] = record[3]

for record in result1:
    if record[0] <= 2:   # 점수 기준
        t = str(record[1])
        t = t[:16]

        if record[3] in d.keys():
            d[record[3]].append(t + "=" + str(record[0]))
        else:
            d[record[3]] = [t + "=" + str(record[0])]

for record in result2:
    if record[0] <= 2:   # 점수 기준
        t = str(record[1])
        t = t[:16]

        if record[3] in d.keys():
            d[record[3]].append(t + "=" + str(record[0]))
        else:
            d[record[3]] = [t + "=" + str(record[0])]


print(d)
print()

for i in d:
    for j in range(len(d[i])):
        date = d[i][j][:4] + "년 " + d[i][j][5:7] + "월 " + d[i][j][8:10] + "일"
        msg = i + "님이 " + date + "에 실시한 재활 점수가 낮아졌습니다. 주의가 필요합니다"
        clear_d[i].append(d[i][j])

        sms_data = {
            "key": "key",
            "userid": "userid",
            "sender": "phoneNumber",
            "receiver": number_d[i],  # d[i]
            "msg": msg
        }

        send_response = requests.post(send_url, data=sms_data)
        print(send_response.json())

print(d)
print(clear_d)

while True:
    try:
        conn = mariadb.connect(
            user="userid",
            password="pw",
            host="IP",
            port=PortNum,
            database="databaseName"
        )
    except mariadb.Error as e:
        print("{}".format(e))
        sys.exit(1)

    d = {}
    cur = conn.cursor()

    cur.execute("select * from physical_rehabilitation")
    result1 = cur.fetchall()

    cur.execute("select * from language_rehabilitation")
    result2 = cur.fetchall()

    cur.execute("select * from members")
    result3 = cur.fetchall()

    for record in result1:
        if record[0] <= 2:
            t = str(record[1])
            t = t[:16]
            tmp = t + "=" + str(record[0])

            if tmp not in clear_d[record[3]]:
                if record[3] in d.keys():
                    d[record[3]].append(tmp)
                else:
                    d[record[3]] = [tmp]

    for record in result2:
        if record[0] <= 2:  # 점수 기준
            t = str(record[1])
            t = t[:16]
            tmp = t + "=" + str(record[0])

            if tmp not in clear_d[record[3]]:
                if record[3] in d.keys():
                    d[record[3]].append(tmp)
                else:
                    d[record[3]] = [tmp]

    for i in d:
        for j in range(len(d[i])):
            date = d[i][j][:4] + "년 " + d[i][j][5:7] + "월 " + d[i][j][8:10] + "일"
            msg = i + "님이 " + date + "에 실시한 재활 점수가 낮아졌습니다. 주의가 필요합니다"
            clear_d[i].append(d[i][j])

            sms_data = {
                "key": "key",
                "userid": "userid",
                "sender": "phoneNumber",
                "receiver": number_d[i],  # d[i]
                "msg": msg
            }

            send_response = requests.post(send_url, data=sms_data)
            print(send_response.json())
            clear_d[i].append(d[i][j])

    conn.close()


