import { toast } from 'react-toastify';
/**
 * Make HTTP request to API
 * @author Louis Figes
 */
const BASE_URL = 'http://localhost:8000/';


async function handleResponse(response) {
    const contentType = response.headers.get("Content-Type");

    let responseBody;
    if (contentType && contentType.includes("application/json")) {
        responseBody = await response.json();
    } else {
        responseBody = await response.text();
    }

    const standardizedResponse = {
        success: response.ok,
        status: response.status,
        statusText: response.statusText,
        data: responseBody,
    };

    if (!response.ok) {
        standardizedResponse.error = responseBody.message || "An error occurred";
    }

    return standardizedResponse;
}

export async function get(endpoint, headers = {}) {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
        method: 'GET',
        headers: {
            ...headers
        }
    });
    return handleResponse(response);
}

export async function post(endpoint, data, headers = {}) {
    return httpRequest('POST', endpoint, data, headers);
}

export async function put(endpoint, data, headers = {}) {
    return httpRequest('PUT', endpoint, data, headers);
}

export async function del(endpoint, data, headers = {}) {
    return httpRequest('DELETE', endpoint, data, headers);
}

/**
 * Modified to handle when backend is down
 * @author Louis Figes
 */
async function httpRequest(method, endpoint, data, headers = {}) {
    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, {
            method,
            headers: {
                ...headers
            },
            body: data ? data : undefined
        });
        return handleResponse(response);
        // eslint-disable-next-line no-unused-vars
    } catch(error) {
        toast.error("Unable to connect to NE Electricity, try again later");
        return {
            success: false,
            status: 500,
            statusText: "Internal Server Error",
            data: { cause: "Unable to connect to NE Electricity, try again later" }
        };
    }

}