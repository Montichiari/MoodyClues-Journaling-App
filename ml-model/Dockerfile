# Use a slim Python image
#FROM python:3.9-slim
FROM python:3.11-slim

# Set the working directory
WORKDIR /app

# Copy the requirements.txt and install dependencies
COPY requirements.txt ./

RUN pip install --no-cache-dir -r requirements.txt
RUN pip install --no-cache-dir torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu118

# Copy the Flask app and model
COPY . /app

# Expose the port for the Flask app
EXPOSE 5000

# Run the Flask app
CMD ["python", "app.py"]

# Set the default command to run the Flask app using Gunicorn
#CMD ["gunicorn", "-w", "4", "-b", "0.0.0.0:5000", "app:app"]
