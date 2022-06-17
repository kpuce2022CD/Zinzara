import requests
import json
import mariadb
import sys
import time
import pandas as pd

send_url = "https://apis.aligo.in/send/"

try:
    conn = mariadb.connect(
        user="user",
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

cur.execute("select * from members")
result2 = cur.fetchall()

l = []
d = {}

# data = pd.DataFrame.from_records(res)
# print(data)

for record in result1:
    if record[0] <= 2:
        l.append(record[3])

print(l)

for i in range(len(l)):
    for record in result2:
        if l[i] == record[0]:
            d[l[i]] = record[2]

print(d)

while True:
    time.sleep(60)
    for i in d:
        # print(i)
        # print(d[i])
        msg = i + "님의 점수가 낮아졌습니다. 주의가 필요합니다"

        sms_data = {
            "key": "key",
            "userid": "userid",
            "sender": "senderPhoneNumber",
            "receiver": "receiverPhoneNumber",  # d[i]
            "msg": msg
        }

        send_response = requests.post(send_url, data=sms_data)
        print(send_response.json())

