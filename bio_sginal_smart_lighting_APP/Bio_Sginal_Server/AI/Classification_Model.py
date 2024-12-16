from flask import Flask, request, jsonify
import pickle
from feature_extraction import get_X_r
import pandas as pd
import numpy as np

app = Flask(__name__)

# 모델 로드
with open('AI\stress-classifier-model.pkl', 'rb') as f:
    model = pickle.load(f)


def predict_stress(hr, gsr, temp):
     # 데이터프레임 생성
    sample_data = {
        "TEMP": temp,
        "HR": hr,
        "GSR": gsr
    }
    df = pd.DataFrame([sample_data.values()], columns=['TEMP', 'HR', 'GSR'])

    # 데이터 반복 (40번 반복)
    df = pd.concat([df] * 40, ignore_index=True)

    # 특성 추출
    X_r = get_X_r(df)


    # 분류 모델 예측
    classification = model.predict(X_r)
    stress_level_mapping = {0: 'Non-Stress', 1: 'Low-Stress', 2: 'High-Stress'}
    stress_level = stress_level_mapping.get(round(np.mean(classification)), 'Unknown')

    return stress_level

    

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()
    hr = int(data['bpm'])
    gsr = int(data['gsr'])
    temp = float(data['gsr'])
    stress_level = predict_stress(hr, gsr, temp)
    return jsonify({"stressLevel": stress_level})

if __name__ == '__main__':
    app.run(port=5000)
