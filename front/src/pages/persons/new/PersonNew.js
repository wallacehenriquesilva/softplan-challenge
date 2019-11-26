import React from 'react';
import PropTypes from 'prop-types';
import {Alert, Button, ButtonGroup, Col, Form, FormGroup, Input, Label, Row,} from 'reactstrap';

import Widget from '../../../components/Widget';
import {connect} from 'react-redux';
import s from './PersonNew.module.scss';
import {API_BASE_URL_PERSON} from "../../../constants";
import {getPerson} from "../../../util/APIUtils";
import {createRequest} from "../../../actions/persons";

export const cpfMask = value => {
    return value
        .replace(/\D/g, '') // substitui qualquer caracter que nao seja numero por nada
        .replace(/(\d{3})(\d)/, '$1.$2') // captura 2 grupos de numero o primeiro de 3 e o segundo de 1, apos capturar o primeiro grupo ele adiciona um ponto antes do segundo grupo de numero
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d{1,2})/, '$1-$2')
        .replace(/(-\d{2})\d+?$/, '$1') // captura 2 numeros seguidos de um traço e não deixa ser digitado mais nada
}


class PersonNew extends React.Component {
    static propTypes = {
        dispatch: PropTypes.func.isRequired,
        message: PropTypes.string,
        isFetching: PropTypes.bool,
    };

    static defaultProps = {
        isFetching: false,
        message: null,
    };


    constructor(props) {
        super(props);

        console.log("State");
        console.log(JSON.stringify(this.state));
        console.log(this.props.location.url);


        this.onRadioBtnClickOne = this.onRadioBtnClickOne.bind(this);
        this.onRadioBtnClickTwo = this.onRadioBtnClickTwo.bind(this);


        this.state = {
            name: '',
            email: '',
            bornDate: '',
            cpf: '',
            naturality: '',
            nacionality: '',
            rFeminino: null,
            rMasculino: null,
        };

    }


    componentDidMount() {
        if (this.props.location.url) {
            this.getPerson(this.props.location.url);
        }
    }

    getPerson(url) {
        getPerson(url)
            .then(response => {
                this.setState({
                    name: response.name,
                    email: response.email,
                    bornDate: response.bornDate,
                    cpf: response.cpf,
                    naturality: response.naturality,
                    nacionality: response.nacionality,
                    rFeminino: response.sex === 2 ? 2 : null,
                    rMasculino: response.sex === 1 ? 1 : null,
                });

            }).catch(error => {
            this.setState({
                loading: false
            });
        });
    }

    onRadioBtnClickOne(rMasculino) {
        this.state.rFeminino = null;
        this.setState({rMasculino});
    }

    onRadioBtnClickTwo(rFeminino) {
        this.state.rMasculino = null;
        this.setState({rFeminino});
    }

    changeName = (event) => {
        this.setState({name: event.target.value});
    }

    changeEmail = (event) => {
        this.setState({email: event.target.value});
    }

    changeBornDate = (event) => {
        this.setState({bornDate: event.target.value});
    }

    changeCpf = (event) => {
        this.setState({cpf: cpfMask(event.target.value)});
    }
    changeNaturality = (event) => {
        this.setState({naturality: event.target.value});
    }
    changeNacionality = (event) => {
        this.setState({nacionality: event.target.value});
    }


    doCreatePost = (e) => {
        this.props
            .dispatch(
                createRequest({
                    name: this.state.name,
                    email: this.state.email,
                    bornDate: this.state.bornDate,
                    cpf: this.state.cpf,
                    naturality: this.state.naturality,
                    nacionality: this.state.nacionality,
                    sex: this.state.rFeminino ? this.state.rFeminino : this.state.rMasculino
                }, this.props.location.url ? this.props.location.url : API_BASE_URL_PERSON, this.props.location.url ? 'put' : 'post'),
            )
            .then(() =>
                this.setState({
                    name: '',
                    email: '',
                    bornDate: '',
                    cpf: '',
                    naturality: '',
                    nacionality: '',
                    rFeminino: null,
                    rMasculino: null,
                }),
            );
        e.preventDefault();
    }

    render() {
        return (
            <div className={s.root}>
                <h1>{this.props.location.url ? 'Editar pessoa ' : 'Criar nova pessoa'}</h1>
                <Row>
                    <Col sm={12}>
                        <Widget>
                            <Form onSubmit={this.doCreatePost}>
                                {this.props.message && (
                                    <Alert className="alert-sm" bsstyle="info">
                                        {this.props.message}
                                    </Alert>
                                )}
                                <Row>
                                    <Col sm={6}>
                                        <FormGroup>
                                            <Label for="input-title">Nome</Label>
                                            <Input
                                                id="input-title"
                                                type="text"
                                                placeholder="Nome"
                                                value={this.state.name}
                                                required
                                                onChange={this.changeName}
                                            />
                                        </FormGroup>
                                    </Col>
                                    <Col sm={6}>
                                        <FormGroup>
                                            <Label for="input-title">Email</Label>
                                            <Input
                                                id="input-title"
                                                type="email"
                                                placeholder="Email"
                                                value={this.state.email}
                                                onChange={this.changeEmail}
                                            />
                                        </FormGroup>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={6}>
                                        <FormGroup>
                                            <Label for="input-title">Data de Nascimento</Label>
                                            <Input
                                                id="input-title"
                                                type="date"
                                                placeholder="Data de Nascimento"
                                                required
                                                value={this.state.bornDate}
                                                onChange={this.changeBornDate}
                                            />
                                        </FormGroup>
                                    </Col>
                                    <Col sm={6}>
                                        <FormGroup>
                                            <Label for="input-title">CPF</Label>
                                            <Input
                                                id="input-title"
                                                type="text"
                                                placeholder="CPF"
                                                value={this.state.cpf}
                                                required
                                                onChange={this.changeCpf}
                                            />
                                        </FormGroup>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={6}>
                                        <FormGroup>
                                            <Label for="input-title">Naturalidade</Label>
                                            <Input
                                                id="input-title"
                                                type="text"
                                                placeholder="Naturalidade"
                                                value={this.state.naturality}
                                                onChange={this.changeNaturality}
                                            />
                                        </FormGroup>
                                    </Col>
                                    <Col sm={6}>
                                        <FormGroup>
                                            <Label for="input-title">Nacionalidade</Label>
                                            <Input
                                                id="input-title"
                                                type="text"
                                                placeholder="Nacionalidade"
                                                value={this.state.nacionality}
                                                onChange={this.changeNacionality}
                                            />
                                        </FormGroup>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={6}>
                                        <FormGroup>
                                            <Label for="input-title">Sexo</Label>
                                            <br/>
                                            <ButtonGroup>
                                                <Button
                                                    color="danger" onClick={() => this.onRadioBtnClickOne(1)}
                                                    active={this.state.rMasculino === 1}
                                                >Masculino</Button>
                                                <Button
                                                    color="danger" onClick={() => this.onRadioBtnClickTwo(2)}
                                                    active={this.state.rFeminino === 2}
                                                >Feminino</Button>
                                            </ButtonGroup>
                                        </FormGroup>
                                    </Col>
                                </Row>
                                <div className="d-flex justify-content-end">
                                    <ButtonGroup>
                                        <Button color="default">Cancel</Button>
                                        <Button color="danger" type="submit">
                                            Salvar
                                        </Button>
                                    </ButtonGroup>
                                </div>
                            </Form>
                        </Widget>
                    </Col>
                </Row>
            </div>
        )
            ;
    }
}

function mapStateToProps(state) {
    return {
        isFetching: state.persons.isFetching,
        message: state.persons.message,
    };
}

export default connect(mapStateToProps)(PersonNew);
