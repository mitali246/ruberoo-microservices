import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:9085';

export const api = axios.create({
  baseURL: API_BASE_URL
});

export function authApi() {
  const token = localStorage.getItem('token');
  const instance = axios.create({ baseURL: API_BASE_URL });
  instance.interceptors.request.use(cfg => {
    if (token) cfg.headers.set('Authorization', `Bearer ${token}`);
    return cfg;
  });
  return instance;
}

