from flask import Flask, request, jsonify
import torch
import numpy as np
from transformers import DebertaV2Tokenizer
from torch import nn
from transformers import AutoModelForSequenceClassification

# Define the emotion classes
emotion_classes = ['angry', 'sad', 'anxious', 'happy', 'curious', 'confused', 'surprised', 'neutral']

# Load the trained model
model = AutoModelForSequenceClassification.from_pretrained('microsoft/deberta-v3-small', num_labels=8, problem_type='multi_label_classification')
model.load_state_dict(torch.load('deberta_model_best.pt'))
model.eval()

# Load the tuned thresholds
tuned_thresholds = np.load('tuned_thresholds.npy')

# Load tokenizer
tokenizer = DebertaV2Tokenizer.from_pretrained('microsoft/deberta-v3-small')

app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def predict():
    try:
        text = request.json['text']  # Get text from JSON request
        inputs = tokenizer(text, return_tensors='pt', padding=True, truncation=True, max_length=64)
        
        # Make prediction
        with torch.no_grad():
            outputs = model(**inputs)
            probs = torch.sigmoid(outputs.logits).cpu().numpy()

        # Initialize a list to store the top emotions
        top_emotions = []

        # For each sample in the batch
        for i in range(len(probs)):
            # Get the predicted probabilities for the current sample
            sample_probs = probs[i]

            # Get the indices of the top classes (in order of probability)
            sorted_indices = np.argsort(sample_probs)[::-1]  # Sort in descending order
            
            # Apply the logic based on the number of classes that pass the threshold
            pass_threshold_indices = np.where(sample_probs > tuned_thresholds)[0]  # Calculate per sample

            # Determine the top emotions based on how many classes pass the threshold
            if len(pass_threshold_indices) == 2:
                # Rule 1: If 2 classes pass the threshold, show 2 classes
                top_classes = [emotion_classes[sorted_indices[0]], emotion_classes[sorted_indices[1]]]
                # Exclude "neutral" if other emotions are present
                if "neutral" in top_classes:
                    top_classes.remove("neutral")

            elif len(pass_threshold_indices) == 1:
                # Rule 2: If 1 class passes the threshold, show 1 class
                top_classes = [emotion_classes[sorted_indices[0]]]
                # If the only emotion is "neutral", show it
                if "neutral" in top_classes and len(top_classes) == 1:
                    pass  # "neutral" can be shown here

            elif len(pass_threshold_indices) == 0:
                # Rule 3: If no class passes the threshold, use fallback and show top class with highest probability
                top_classes = [emotion_classes[sorted_indices[0]]]
                # If the only emotion is "neutral", show it
                if "neutral" in top_classes and len(top_classes) == 1:
                    pass  # "neutral" can be shown here

            elif len(pass_threshold_indices) >= 3:
                # Rule 4: If 3 or more classes pass the threshold, show top 2 emotions with highest probability
                top_classes = [emotion_classes[sorted_indices[0]], emotion_classes[sorted_indices[1]]]
                # Exclude "neutral" if other emotions are present
                if "neutral" in top_classes:
                    top_classes.remove("neutral")

            # Append the top classes for the current sample
            top_emotions.append(top_classes)

        # Return the result
        return jsonify({"top_emotions": top_emotions})

    except Exception as e:
        return jsonify({"error": str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
