import { useState } from 'react';
import { authApi } from '../services/api';

export default function BookRide() {
  const [pickup, setPickup] = useState('');
  const [dropoff, setDropoff] = useState('');
  const [message, setMessage] = useState('');

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    try {
      const res = await authApi().post('/rides/book', { pickup, dropoff });
      setMessage(`Ride booked: ${res.data?.rideId ?? 'OK'}`);
    } catch (err: any) {
      setMessage(err?.response?.data?.message || 'Booking failed');
    }
  }

  return (
    <form onSubmit={onSubmit} style={{ maxWidth: 480, display: 'grid', gap: 8 }}>
      <h2>Book Ride</h2>
      <input placeholder="Pickup" value={pickup} onChange={e => setPickup(e.target.value)} />
      <input placeholder="Dropoff" value={dropoff} onChange={e => setDropoff(e.target.value)} />
      <button type="submit">Book</button>
      {message && <div>{message}</div>}
    </form>
  );
}

