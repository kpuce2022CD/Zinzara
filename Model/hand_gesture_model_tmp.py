import numpy as np
import tensorflow as tf
import pandas as pd
import missingno as msno
import matplotlib.pyplot as plt
import seaborn as sns
from keras.models import Sequential
from keras.layers import Dense, Dropout, Flatten, Conv2D, MaxPooling2D
from keras.utils import np_utils
from sklearn.preprocessing import LabelEncoder
from keras.models import load_model
from sklearn.model_selection import StratifiedKFold
from keras.callbacks import ModelCheckpoint
from keras.callbacks import EarlyStopping
from sklearn.model_selection import train_test_split
from keras.datasets import mnist
from sklearn.preprocessing import StandardScaler
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score
from sklearn.metrics import accuracy_score
from sklearn import svm, datasets, tree
import math

Data_set = pd.read_csv("hand_gesture_data/hand_gesture_data2.csv")
x = Data_set.iloc[:, :40].astype(float)
y = Data_set.iloc[:, 40]

# 결측치 확인
# msno.matrix(Data_set)
# plt.show()


# 상관관계 분석
corr = Data_set.corr()
print(corr)
print("==========")
print(corr["label"].sort_values(ascending=False))

# plt.rcParams["figure.figsize"] = (10, 10)
sns.heatmap(Data_set.corr(), annot=True)
plt.show()





