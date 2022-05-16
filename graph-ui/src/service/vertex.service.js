const API_URL = process.env.REACT_APP_VERTEX_REGISTRY_API_URL;

export const createVertex = name => {
    return fetch(`${API_URL}/add-vertex`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({name})
    });
}

export const removeVertex = id => {
    return fetch(`${API_URL}/remove-vertex`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({vertexId: id})
    });
}
