const API_URL = process.env.REACT_APP_PATH_FINDING_SERVICE_API_URL;

export const findShortestPath = (sourceId, destinationId) => {
    return fetch(`${API_URL}/shortest-path?source=${sourceId}&destination=${destinationId}`);
}
