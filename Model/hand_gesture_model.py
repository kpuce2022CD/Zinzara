import numpy as np
import tensorflow as tf
import pandas as pd
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

seed=0
np.random.seed(seed)
tf.random.set_seed(seed)

Data_set=np.loadtxt("hand_gesture_data3.csv", delimiter=",")
X=Data_set[:,0:40].astype(float)
Y_obj=Data_set[:,40]

e=LabelEncoder()
e.fit(Y_obj)
Y=e.transform(Y_obj)

Y_encoded=np_utils.to_categorical(Y)

print(X[0])
print(X[0].shape)   # (40,)


model=Sequential()
model.add(Dense(128, input_dim=40, activation="relu"))
model.add(Dense(64, activation="sigmoid"))
model.add(Dense(32, activation="relu"))
model.add(Dense(24, activation="sigmoid"))
model.add(Dense(16, activation="relu"))
model.add(Dense(10, activation="softmax"))

model.compile(loss="categorical_crossentropy", optimizer="adam", metrics=["accuracy"])
model.fit(X, Y_encoded, epochs=50, batch_size=10)

model.save("model_test.h5")
del model
model=load_model("model_test.h5")
print("Accuracy : {}".format(model.evaluate(X, Y_encoded)[1]))

print(model.summary())
