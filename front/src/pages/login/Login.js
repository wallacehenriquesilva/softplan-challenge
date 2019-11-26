import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Redirect, withRouter} from 'react-router-dom';
import {connect} from 'react-redux';
import {Alert, Button, Col, Form, FormGroup, Input, Row} from 'reactstrap';

import './Login.css';


import s from './Login.module.scss';
import Widget from '../../components/Widget';
import Footer from "../../components/Footer";
import {loginUser} from '../../actions/user';
import jwt from 'jsonwebtoken';
import config from '../../config'
import {API_BASE_URL, FACEBOOK_AUTH_URL, GITHUB_AUTH_URL, GOOGLE_AUTH_URL} from '../../constants';
import fbLogo from '../../images/fb-logo.png';
import googleLogo from '../../images/google-logo.png';
import githubLogo from '../../images/github-logo.png';
import {createUser} from "../../util/APIUtils";


class Login extends React.Component {

    static propTypes = {
        dispatch: PropTypes.func.isRequired,
        isAuthenticated: PropTypes.bool,
        isFetching: PropTypes.bool,
        location: PropTypes.any, // eslint-disable-line
        errorMessage: PropTypes.string,
    };

    static defaultProps = {
        isAuthenticated: false,
        isFetching: false,
        location: {},
        errorMessage: null,
    };

    static isAuthenticated(token) {
        // We check if app runs with backend mode
        if (!config.isBackend && token) return true;
        if (!token) return;
        const date = new Date().getTime() / 1000;
        const data = jwt.decode(token);
        return date < data.exp;
    }

    constructor(props) {
        super(props);

        this.state = {
            login: '',
            username: '',
            password: '',
            registre: false
        };
    }

    changeLogin = (event) => {
        this.setState({login: event.target.value});
    };
    changeUsername = (event) => {
        this.setState({username: event.target.value});
    };

    changePassword = (event) => {
        this.setState({password: event.target.value});
    };

    doLogin = (e) => {
        if (!this.state.registre) {
            let response = loginUser({
                login: this.state.login,
                password: this.state.password,
            });
            this.props.dispatch(response);
            e.preventDefault();
        } else {
            this.registreNewUser();
        }
    };


    registreNewUser = () => {
        let retorno = createUser(
            {
                email: this.state.login,
                password: this.state.password,
                username: this.state.username
            }, API_BASE_URL + "/api/v1/auth/signup", 'POST');

        console.log("Retorno do cadsatro" + retorno);
        this.newUser();
    };

    newUser = () => {
        this.setState({registre: !this.state.registre});
    };

    render() {
        const {from} = this.props.location.state || {
            from: {pathname: '/app'},
        };

        if (this.props.isAuthenticated) {
            return <Redirect to={from}/>;
        }

        return (
            <div className={s.root}>
                <Row>
                    <Col xs={{size: 10, offset: 1}} sm={{size: 6, offset: 3}} lg={{size: 4, offset: 4}}>
                        <Widget className={s.widget}>
                            <h4 className="mt-0">Login</h4>
                            <Form className="mt" onSubmit={this.doLogin}>
                                {this.props.errorMessage && (
                                    <Alert size="sm" color="danger">
                                        {this.props.errorMessage}
                                    </Alert>
                                )}
                                {this.state.registre ? (<FormGroup className="form-group">
                                    <Input
                                        className="no-border"
                                        value={this.state.username}
                                        onChange={this.changeUsername}
                                        type="text"
                                        required
                                        name="username"
                                        placeholder="Nome"
                                    />
                                </FormGroup>) : (<br/>)}
                                <FormGroup className="form-group">
                                    <Input
                                        className="no-border"
                                        value={this.state.login}
                                        onChange={this.changeLogin}
                                        type="email"
                                        required
                                        name="email"
                                        placeholder="Email"
                                    />
                                </FormGroup>
                                <FormGroup>
                                    <Input
                                        className="no-border"
                                        value={this.state.password}
                                        onChange={this.changePassword}
                                        type="password"
                                        required
                                        name="password"
                                        placeholder="Senha"
                                    />
                                </FormGroup>
                                <div className="d-flex justify-content-between align-items-center">
                                    <div>
                                        <Button onClick={() => this.newUser()} color="default" size="sm">
                                            {this.state.registre ? 'Cancelar' : 'Criar conta'}
                                        </Button>
                                        {!this.state.registre ? (
                                            <Button color="success" size="sm" type="submit">
                                                Login
                                            </Button>) : (
                                            <Button type="submit" color="success" size="sm">
                                                Cadastrar
                                            </Button>)}
                                    </div>
                                </div>
                            </Form>
                            <SocialLogin/>
                            <GitReference/>
                        </Widget>
                    </Col>
                </Row>
                <Footer className="text-center"/>
            </div>
        );
    }
}

class SocialLogin extends Component {
    render() {
        return (
            <div className="social-login">
                <ul>
                    <li>
                        <a className="btn btn-block social-btn google" href={GOOGLE_AUTH_URL}>
                            <img src={googleLogo} alt="Google"/></a>
                    </li>
                    <li>
                        <a className="btn btn-block social-btn facebook" href={FACEBOOK_AUTH_URL}>
                            <img src={fbLogo} alt="Facebook"/></a>
                    </li>
                    <li>
                        <a className="btn btn-block social-btn github" href={GITHUB_AUTH_URL}>
                            <img src={githubLogo} alt="Github"/></a>
                    </li>
                </ul>
            </div>
        );
    }
}

export class GitReference extends Component {
    render() {
        return (
            <a href={"https://github.com/wallacehenriquesilva/softplan-challenge"}>
                Source Code
            </a>
        );
    }
}


function mapStateToProps(state) {
    return {
        isFetching: state.auth.isFetching,
        isAuthenticated: state.auth.isAuthenticated,
        errorMessage: state.auth.errorMessage,
    };
}

export default withRouter(connect(mapStateToProps)(Login));

