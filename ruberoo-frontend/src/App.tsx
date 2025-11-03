import { Link, Route, Routes, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import BookRide from './pages/BookRide';
import TrackRide from './pages/TrackRide';

export default function App() {
  return (
    <div style={{ fontFamily: 'Inter, system-ui, Avenir, Helvetica, Arial, sans-serif', padding: 16 }}>
      <header style={{ display: 'flex', gap: 12, alignItems: 'center', marginBottom: 16 }}>
        <h1 style={{ margin: 0, fontSize: 20 }}>Ruberoo</h1>
        <nav style={{ display: 'flex', gap: 12 }}>
          <Link to="/login">Login</Link>
          <Link to="/register">Register</Link>
          <Link to="/book">Book Ride</Link>
          <Link to="/track">Track</Link>
        </nav>
      </header>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/book" element={<BookRide />} />
        <Route path="/track" element={<TrackRide />} />
      </Routes>
    </div>
  );
}
import { Link, Route, Routes, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import BookRide from './pages/BookRide';
import TrackRide from './pages/TrackRide';

export default function App() {
  return (
    <div style={{ fontFamily: 'Inter, system-ui, Avenir, Helvetica, Arial, sans-serif', padding: 16 }}>
      <header style={{ display: 'flex', gap: 12, alignItems: 'center', marginBottom: 16 }}>
        <h1 style={{ margin: 0, fontSize: 20 }}>Ruberoo</h1>
        <nav style={{ display: 'flex', gap: 12 }}>
          <Link to="/login">Login</Link>
          <Link to="/register">Register</Link>
          <Link to="/book">Book Ride</Link>
          <Link to="/track">Track</Link>
        </nav>
      </header>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/book" element={<BookRide />} />
        <Route path="/track" element={<TrackRide />} />
      </Routes>
    </div>
  );
}

