const API_BASE = '/api';


async function apiFetch(endpoint, options = {}) {
  const response = await fetch(`${API_BASE}${endpoint}`, {
    ...options,
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    },
    ...options
  })

  if (response.status === 401) {
    throw new Error('UNAUTHORIZED')
  }

  if (!response.ok) {
    const text = await response.text()
    throw new Error(text || 'Greška na serveru')
  }

  return response
}

export default {

  get(url) {
    return apiFetch(url)
  },

  post(url, body) {
    return apiFetch(url, {
      method: 'POST',
      body: JSON.stringify(body)
    })
  },

  put(url, body) {
    return apiFetch(url, {
      method: 'PUT',
      body: JSON.stringify(body)
    })
  },

  delete(url) {
    return apiFetch(url, {
      method: 'DELETE'
    })
  }

}
