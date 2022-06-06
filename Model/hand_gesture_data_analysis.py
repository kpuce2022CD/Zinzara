import pandas as pd
import missingno as msno
import matplotlib.pyplot as plt
import seaborn as sns

Data_set = pd.read_csv("hand_gesture_data/hand_gesture_data2.csv")
x = Data_set.iloc[:, :40].astype(float)
y = Data_set.iloc[:, 40]

# 결측치 확인
msno.matrix(Data_set)
plt.show()


# 상관관계 분석
corr = Data_set.corr()
print(corr)
print("==========")
print(corr["label"].sort_values(ascending=False))

# plt.rcParams["figure.figsize"] = (10, 10)
sns.heatmap(Data_set.corr(), annot=True)
plt.show()





