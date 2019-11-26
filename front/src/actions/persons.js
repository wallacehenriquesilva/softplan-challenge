export const CREATE_PERSON_INITIAL = 'CREATE_PERSON_INITIAL';
export const CREATE_PERSON_REQUEST = 'CREATE_PERSON_REQUEST';
export const CREATE_PERSON_SUCCESS = 'CREATE_PERSON_SUCCESS';
export const CREATE_PERSON_FAILURE = 'CREATE_PERSON_FAILURE';
export const FETCH_PERSONS_REQUEST = 'FETCH_PERSONS_REQUEST';
export const FETCH_PERSONS_SUCCESS = 'FETCH_PERSONS_SUCCESS';
export const FETCH_PERSONS_FAILURE = 'FETCH_PERSONS_FAILURE';

function createPostInitial() {
    return {
        type: CREATE_PERSON_INITIAL,
        isFetching: false,
    };
}

function requestCreatePerson(post) {
    return {
        type: CREATE_PERSON_REQUEST,
        isFetching: true,
        post,
    };
}

function createPersonSuccess(post) {
    return {
        type: CREATE_PERSON_SUCCESS,
        isFetching: false,
        post,
    };
}

function createPersonError(message) {
    return {
        type: CREATE_PERSON_FAILURE,
        isFetching: true,
        message,
    };
}

export function createRequest(postData, link, method) {
    const config = {
        method: method,
        headers: {
            Accept: 'application/json',
            "Content-Type": 'application/json',
            Authorization: "Bearer " + localStorage.getItem('id_token')
        },
        body: JSON.stringify(postData),
        credentials: 'include',
    };

    return dispatch => {
        dispatch(requestCreatePerson(postData));
        return fetch(link, config)
            .then(response => response.json().then(post => ({post, response})))
            .then(({post, response}) => {
                if (!response.ok) {
                    dispatch(createPersonError(post.message));
                    return Promise.reject(post);
                }
                dispatch(createPersonSuccess(post));
                setTimeout(() => {
                    dispatch(createPostInitial());
                }, 5000);
                return Promise.resolve(post);
            })
            .catch(err => console.error('Error: ', err));

    };
}
