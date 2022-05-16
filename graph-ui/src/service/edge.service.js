const API_URL = process.env.REACT_APP_VERTEX_REGISTRY_API_URL;

export const createEdge = (weight, from, to) => {
    return fetch(`${API_URL}/add-edge`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({weight, from, to})
    });
}

export const removeEdge = id => {
    return fetch(`${API_URL}/remove-edge`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({edgeId: id})
    });
}
