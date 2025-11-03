import { useState } from 'react';
import { api } from '../services/api';

export default function Register() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState<'PASSENGER' | 'DRIVER'>('PASSENGER');
  const [message, setMessage] = useState<string>('');

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    try {
      await api.post('/users/register', { email, password, role });
      setMessage('Registered');
    } catch (err: any) {
      setMessage(err?.response?.data?.message || 'Registration failed');
    }
  }

  return (
    <form onSubmit={onSubmit} style={{ maxWidth: 360, display: 'grid', gap: 8 }}>
      <h2>Register</h2>
      <input placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} />
      <input placeholder="Password" type="password" value={password} onChange={e => setPassword(e.target.value)} />
      <select value={role} onChange={e => setRole(e.target.value as any)}>
        <option value="PASSENGER">Passenger</option>
        <option value="DRIVER">Driver</option>
      </select>
      <button type="submit">Create account</button>
      {message && <div>{message}</div>}
    </form>
  );
}

