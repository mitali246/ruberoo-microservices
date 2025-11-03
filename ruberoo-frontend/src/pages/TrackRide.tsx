import { useEffect, useState } from 'react';
import { authApi } from '../services/api';

export default function TrackRide() {
  const [rideId, setRideId] = useState('');
  const [positions, setPositions] = useState<Array<{ lat: number; lng: number; ts: string }>>([]);

  async function fetchTrack() {
    if (!rideId) return;
    const res = await authApi().get(`/tracking/${rideId}`);
    setPositions(res.data?.positions ?? []);
  }

  useEffect(() => {
    const t = setInterval(fetchTrack, 5000);
    return () => clearInterval(t);
  }, [rideId]);

  return (
    <div style={{ maxWidth: 600, display: 'grid', gap: 8 }}>
      <h2>Track Ride</h2>
      <input placeholder="Ride ID" value={rideId} onChange={e => setRideId(e.target.value)} />
      <button onClick={fetchTrack}>Refresh</button>
      <ul>
        {positions.map((p, idx) => (
          <li key={idx}>{p.ts}: ({p.lat}, {p.lng})</li>
        ))}
      </ul>
    </div>
  );
}

