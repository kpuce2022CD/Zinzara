import numpy as np
import tensorflow as tf
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from keras.models import Sequential
from keras.layers import Dense, Dropout, Flatten, Conv2D, MaxPooling2D, Activation, BatchNormalization
from keras.utils import np_utils
from sklearn.preprocessing import LabelEncoder
from keras.models import load_model
from keras.callbacks import ModelCheckpoint
from keras.callbacks import EarlyStopping
from sklearn.model_selection import train_test_split, StratifiedKFold
from keras.datasets import mnist
from sklearn.preprocessing import StandardScaler
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score
from sklearn.metrics import accuracy_score
from sklearn import svm, datasets, tree


seed = 0
np.random.seed(seed)
tf.random.set_seed(seed)

Data_set = pd.read_csv("hand_gesture_data/hand_gesture_data2.csv")
x = Data_set.iloc[:, 20:45].astype(float)
y = Data_set.iloc[:, 45]

# e = LabelEncoder()
# e.fit(Y_obj)
# Y = e.transform(Y_obj)

y = np_utils.to_categorical(y)

x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, shuffle=True, stratify=y, random_state=1)


model = Sequential()


# model.add(Dense(64, input_dim=25, activation="relu"))
# model.add(Dense(128, activation="relu"))
# model.add(Dropout(0.25))
# model.add(Dense(256, activation="relu"))
# model.add(Dense(128, activation="relu"))
# model.add(Dense(64, activation="relu"))
# model.add(Dropout(0.25))
# model.add(Dense(32, activation="relu"))
# model.add(Dense(10, activation="softmax"))


model.add(Dense(64, input_dim=25))
model.add(Activation("relu"))
model.add(Dropout(0.25))
model.add(Dense(128))
model.add(Activation("relu"))
model.add(Dropout(0.25))
model.add(Dense(256))
model.add(Activation("relu"))
model.add(Dropout(0.25))
model.add(Dense(128))
model.add(Activation("relu"))
model.add(Dropout(0.25))
model.add(Dense(64))
model.add(Activation("relu"))
model.add(Dropout(0.25))
model.add(Dense(32))
model.add(Activation("relu"))
model.add(Dropout(0.25))
model.add(Dense(10))
model.add(Activation("softmax"))


model.compile(loss="categorical_crossentropy", optimizer="adam", metrics=["accuracy"])
history = model.fit(x_train, y_train, epochs=60, batch_size=10, validation_split=0.2)
print("Accuracy : {}".format(model.evaluate(x_test, y_test)[1]))


plt.subplot(211)
plt.plot(history.history["accuracy"])
plt.plot(history.history["val_accuracy"])
plt.title("Accuracy")
plt.xlabel("epoch")
plt.ylabel("accuracy")
plt.legend(["train", "validation"], loc="lower right")

plt.subplot(212)
plt.plot(history.history["loss"])
plt.plot(history.history["val_loss"])
plt.title("Loss")
plt.xlabel("epoch")
plt.ylabel("loss")
plt.legend(["train", "validation"], loc="upper right")
plt.tight_layout()
plt.show()


# model.save("model_test.h5")
# del model
# model = load_model("model_test.h5")
# print("Accuracy : {}".format(model.evaluate(x, y)[1]))
# print("Accuracy : {}".format(model.evaluate(x, y)))










