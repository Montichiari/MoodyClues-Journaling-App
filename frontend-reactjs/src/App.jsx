import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import './App.css'
import Login from './pages/Login'

function App() {

  return (

      <BrowserRouter>
          <Routes>
              <Route path="/login" element={<Login />} />

              <Route path="/journal/mood"
                     element={<ProtectedRoute> <Mood /> </ProtectedRoute>} />

              <Route path="/journal/habits"
                     element={<ProtectedRoute> <Habits /> </ProtectedRoute>} />

              <Route path="/journal/reflections"
                     element={<ProtectedRoute> <Reflections /> </ProtectedRoute>} />
          </Routes>
      </BrowserRouter>
  )
}

export default App

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<App />);
