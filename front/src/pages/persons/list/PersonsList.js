import React from 'react';
import PropTypes from 'prop-types';
import {Link} from 'react-router-dom';
import {connect} from 'react-redux';
import {Button, Input, InputGroup, Table,} from 'reactstrap';

import s from './PersonsList.module.scss';
import Widget from '../../../components/Widget';
import Row from "reactstrap/es/Row";
import Col from "reactstrap/es/Col";
import {deletePersonRequest, getAllPersons} from "../../../util/APIUtils";
import uuid from "uuid/v4";
import {toast} from "react-toastify";

class PersonsList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            checkboxes3: [false, false, false, true, false, false],
            personsList: [],
            personsInitialList: [],
            options: {
                position: "top-right",
                autoClose: 5000,
                closeOnClick: false,
                pauseOnHover: false,
                draggable: true
            }
        }
    }


    componentDidMount() {
        this.getPersons();
    }

    getPersons() {
        getAllPersons()
            .then(response => {
                this.setState({
                    personsList: response,
                    personsInitialList: response
                });

            }).catch(error => {
            this.setState({
                loading: false
            });
        });
    }

    static propTypes = {
        dispatch: PropTypes.func.isRequired,
        posts: PropTypes.array,
        isFetching: PropTypes.bool,
    };

    static defaultProps = {
        isFetching: false,
        posts: [],
    };

    static meta = {
        title: 'Posts list',
        description: 'About description',
    };

    deletePerson = (link, name) => {
        this.deletePersonAlert(name, link);
    };

    deletePersonAlert = (name, link) => {
        let id = uuid();
        toast.info(
            <div className="d-flex flex-column align-items-center">
                Deseja apagar a pessoa {name} ?
                <Button onClick={() => this.deletePersonAlertFinalize(id, name, link)} outline color="default" size="sm"
                        className="width-100 mb-xs mr-xs mt-1 ml-4">Apagar</Button>
            </div>,
            {...this.state.options, toastId: id},
        );
    };

    deletePersonAlertFinalize = (id, name, link) => {
        deletePersonRequest(link)
            .then(response => {
                this.getPersons();
                toast.update(id, {
                    ...this.state.options,
                    render: "Pessoa " + name + " apagada com sucesso!",
                    type: toast.TYPE.SUCCESS
                });
            }).catch(error => {
            toast.update(id, {
                ...this.state.options,
                render: "Não foi possível apagar a pessoa " + name,
                type: toast.TYPE.ERROR
            })
        });


    };

    renderRowPerson(row) {
        let linkDelete = row.links.filter(link => link.rel === 'delete').map(link => link.href);
        return (<tr>
            <td className="text-center">{row.name}</td>
            <td className="text-center">{row.email}</td>
            <td className="text-center">{row.cpf}</td>
            <td className="text-center">{row.naturality}</td>
            <td className="text-center">{row.nacionality}</td>
            <td className="text-center">{row.sex === 1 ? 'M' : 'F'}</td>
            <td className="text-center">

                <Link
                    to={{
                        pathname: "/app/persons/new",
                        url: row.links.filter(link => link.rel === 'update').map(link => link.href),
                    }}
                >
                    <i
                        style={{size: '14px'}}
                        className="fa fa-pencil mr-xs opacity-70"/>
                </Link>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a onClick={() => this.deletePerson(linkDelete, row.name)}><i className="fa fa-trash mr-xs opacity-70"/></a>
            </td>
        </tr>)
    }

    onFilteredChange(filtered) {
        if (filtered.length > 1 && this.state.filterAll.length) {
            const filterAll = '';
            this.setState({filtered: filtered.filter((item) => item.id != 'all'), filterAll})
        } else
            this.setState({filtered});
    }

    filterList(event){
        var updatedList = this.state.personsInitialList;
        updatedList = updatedList.filter(function(item){
            return item.name.toLowerCase().search(
                event.target.value.toLowerCase()) !== -1;
        });
        this.setState({personsList: updatedList});
    };


    filterAll(e) {
        console.log(e.target.value);
        console.log(this.state.personsList);
        let value = e.target.value;
        let list = this.state.personsList.search(op => String.valueOf(op.name).include(value));

        this.setState({personsList: list});
    }

    render() {
        return (
            <div className={s.root}>

                <h1>Pessoas</h1>
                <Widget
                    className="pb-0"
                    title={
                        <div>

                            <div className="pull-right mt-n-xs">
                                <Link
                                    to={{
                                        pathname: "/app/persons/new"
                                    }}
                                    className="btn btn-sm btn-inverse">
                                    Criar nova pessoa
                                </Link>
                            </div>

                            <Row>

                                <Col sm={12} md={6}>
                                    <h5 className="mt-0">
                                        <InputGroup>
                                            <Input placeholder="Procurar pessoa..."
                                                   // onChange={(event) => this.filterAll(event)}/>
                                                   onChange={(event) => this.filterList(event)}/>
                                        </InputGroup>
                                    </h5>
                                </Col>
                            </Row>
                        </div>
                    }
                >
                    <div className="widget-table-overflow">
                        <Table className="table-striped table-lg mt-lg mb-0">
                            <thead>
                            <tr>
                                <th className="text-center">Nome</th>
                                <th className="text-center">Email</th>
                                <th className="text-center">CPF</th>
                                <th className="text-center">Naturalidade</th>
                                <th className="text-center">Nacionalidade</th>
                                <th className="text-center">Sexo</th>
                                <th className="text-center">#</th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.state.personsList.map((row) => this.renderRowPerson(row), this)}
                            </tbody>
                        </Table>
                    </div>
                </Widget>
            </div>
        );
    }
}

function mapStateToProps(state) {
    return {
        isFetching: state.persons.isFetching,
        posts: state.persons.posts,
    };
}

export default connect(mapStateToProps)(PersonsList);
