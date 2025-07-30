# Use a slim Python image
FROM python:3.9-slim

# Set the working directory
WORKDIR /app

# Copy the requirements.txt and install dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy the Flask app and model
COPY . .

# Expose the port for the Flask app
EXPOSE 5000

# Run the Flask app
CMD ["python", "app.py"]
