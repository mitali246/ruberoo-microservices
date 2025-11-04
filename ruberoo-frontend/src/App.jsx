import { useEffect, useMemo, useState } from 'react';

function App() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [token, setToken] = useState(() => localStorage.getItem('auth_token') || '');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const isAuthenticated = useMemo(() => Boolean(token), [token]);

  useEffect(() => {
    if (token) {
      localStorage.setItem('auth_token', token);
    } else {
      localStorage.removeItem('auth_token');
    }
  }, [token]);

  async function handleLogin(e) {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      // Replace this with your real auth endpoint via gateway if available
      // Example: const res = await fetch('/api/auth/login', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ username, password }) });
      // const data = await res.json(); setToken(data.token);

      // Demo: accept any non-empty username/password and issue a fake token
      if (!username || !password) {
        throw new Error('Username and password are required');
      }
      const fakeToken = btoa(`${username}:${Date.now()}`);
      setToken(fakeToken);
      setUsername('');
      setPassword('');
    } catch (err) {
      setError(err?.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  }

  function handleLogout() {
    setToken('');
  }

  async function fetchProtected() {
    setError('');
    setLoading(true);
    try {
      // Replace with a real protected endpoint behind API Gateway when available
      // const res = await fetch('/api/protected', { headers: { Authorization: `Bearer ${token}` } });
      // const data = await res.json(); alert(JSON.stringify(data, null, 2));
      await new Promise(r => setTimeout(r, 500));
      alert('Protected data fetched successfully (demo)');
    } catch (err) {
      setError(err?.message || 'Failed to fetch protected data');
    } finally {
      setLoading(false);
    }
  }

  if (!isAuthenticated) {
    return (
      <div style={{ maxWidth: 420, margin: '10vh auto', padding: 24, border: '1px solid #ddd', borderRadius: 12 }}>
        <h2>Login</h2>
        <form onSubmit={handleLogin}>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <button disabled={loading} type="submit">
              {loading ? 'Signing in...' : 'Sign In'}
            </button>
            {error ? <div style={{ color: 'crimson' }}>{error}</div> : null}
          </div>
        </form>
      </div>
    );
  }

  return (
    <div style={{ maxWidth: 780, margin: '6vh auto', padding: 24 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h2>Dashboard</h2>
        <button onClick={handleLogout}>Logout</button>
      </div>
      <div style={{ marginTop: 12 }}>
        <small style={{ color: '#666' }}>Token (demo): {token.slice(0, 8)}...{token.slice(-6)}</small>
      </div>
      <div style={{ marginTop: 24, display: 'flex', gap: 12 }}>
        <button disabled={loading} onClick={fetchProtected}>Fetch Protected Data</button>
        {loading ? <span>Loading...</span> : null}
        {error ? <span style={{ color: 'crimson' }}>{error}</span> : null}
      </div>
      <div style={{ marginTop: 28 }}>
        <p>Replace demo actions with real API calls once the authentication backend is wired.</p>
      </div>
    </div>
  );
}

export default App;


