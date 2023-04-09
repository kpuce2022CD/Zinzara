import numpy as np
import tensorflow as tf
import pandas as pd
from keras.utils import np_utils
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import GridSearchCV


seed = 0
np.random.seed(seed)
tf.random.set_seed(seed)

Data_set = pd.read_csv("hand_gesture_data/hand_gesture_data2.csv")
x = Data_set.iloc[:, 20:45].astype(float)
y = Data_set.iloc[:, 45]

y = np_utils.to_categorical(y)

x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, shuffle=True, stratify=y, random_state=1)

params = {
    "min_samples_leaf": range(1, 10),
    "max_depth": range(3, 10),
    "min_samples_split": range(2, 7)
}

clf = DecisionTreeClassifier(random_state=1)
gs = GridSearchCV(clf, params, cv=3, verbose=3)
gs.fit(x_train, y_train)

print("gs.best_params_ : ", gs.best_params_)
print("gs.best_score_ :", gs.best_score_)

clf = gs.best_estimator_
clf.fit(x_train, y_train)
print(clf.score(x_train, y_train))
print(clf.score(x_test, y_test))










