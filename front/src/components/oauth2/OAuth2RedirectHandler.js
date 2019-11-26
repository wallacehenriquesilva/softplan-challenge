import React from 'react';
import {Redirect} from 'react-router-dom'
import {getCurrentUser} from "../../util/APIUtils";

class OAuth2RedirectHandler extends React.Component {
    constructor(props) {
        super(props);
        console.log(props);
    }

    getUrlParameter(name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        var results = regex.exec(window.location.href);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    render() {
        let tokenSalvo = localStorage.getItem('id_token');
        let token;
        if (tokenSalvo) {
            token = tokenSalvo;
        } else {
            token = this.getUrlParameter('token');
        }
        const error = this.getUrlParameter('error');

        if (token) {
            localStorage.setItem('id_token', token);
            getCurrentUser()
                .then(response => {
                    this.setState({
                        currentUser: response,
                        authenticated: true,
                        loading: false
                    });
                    localStorage.setItem("currentUser", JSON.stringify(response));
                }).catch(error => {
                this.setState({
                    loading: false
                });
            });

            return <Redirect to={{
                pathname: "/app/main",
                state: {from: this.props.location}
            }}/>;
        } else {
            return <Redirect to={{
                pathname: "/login",
                state: {
                    from: this.props.location,
                    error: error
                }
            }}/>;
        }
    }
}

export default (OAuth2RedirectHandler);
