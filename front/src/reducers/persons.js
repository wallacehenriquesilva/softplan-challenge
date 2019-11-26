import {
    CREATE_PERSON_FAILURE,
    CREATE_PERSON_SUCCESS,
    CREATE_PERSON_INITIAL,
    CREATE_PERSON_REQUEST,
    FETCH_PERSONS_FAILURE,
    FETCH_PERSONS_REQUEST,
    FETCH_PERSONS_SUCCESS,
} from '../actions/persons';


export default function persons(
    state = {
        isFetching: false,
    },
    action,
) {
    switch (action.type) {
        case CREATE_PERSON_INITIAL:
            return Object.assign({}, state, {
                isFetching: false,
                message: null,
            });
        case CREATE_PERSON_REQUEST:
            return Object.assign({}, state, {
                isFetching: true,
            });
        case CREATE_PERSON_SUCCESS:
            return Object.assign({}, state, {
                isFetching: false,
                message: 'Pessoa cadastrada com sucesso',
            });
        case CREATE_PERSON_FAILURE:
            return Object.assign({}, state, {
                isFetching: false,
                message:
                    'Erro ao castrar pessoa ' + action.message,
            });
        case FETCH_PERSONS_REQUEST:
            return Object.assign({}, state, {
                isFetching: true,
            });
        case FETCH_PERSONS_SUCCESS:
            return Object.assign({}, state, {
                isFetching: false,
                posts: action.posts,
            });
        case FETCH_PERSONS_FAILURE:
            return Object.assign({}, state, {
                isFetching: false,
                message: 'Something wrong happened. Please come back later',
            });
        default:
            return state;
    }
}
