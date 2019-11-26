import {ACCESS_TOKEN, API_BASE_URL} from '../constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });

    if (localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', "Bearer " + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response =>
            response.json().then(json => {
                if (!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            })
        );
};


export function getCurrentUser() {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    console.log("Pegando dados do usuário");
    return request({
        url: API_BASE_URL + "/api/v1/users/me",
        method: 'GET'
    });
}

export function getAllUsers() {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    console.log("Pegando dados de todos os usuários");
    return request({
        url: API_BASE_URL + "/api/v1/users/findAll",
        method: 'GET'
    });
}

export function getAllPersons() {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    return request({
        url: API_BASE_URL + "/api/v1/service/data/persons/",
        method: 'GET'
    });
}

export function deletePersonRequest(url) {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    console.log("Apagando pessoa dados de todas as pessoas");
    return request({
        url: url,
        method: 'DELETE'
    });


}

export function getPerson(url) {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    return request({
        url: url,
        method: 'GET'
    });
}

export function createUser(body, url, method) {
    console.log("BOA")
    return request({
        url: url,
        method: method,
        body: JSON.stringify(body)
    });
}
