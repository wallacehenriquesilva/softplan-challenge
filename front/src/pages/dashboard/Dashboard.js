import React, {Component} from 'react';
import cx from 'classnames';
import PropTypes from 'prop-types';
import {Link} from 'react-router-dom';
import {connect} from 'react-redux';
import {Badge, Breadcrumb, BreadcrumbItem, Col, Row, Table} from 'reactstrap';

import Widget from '../../components/Widget';
import s from './Dashboard.module.scss';
import {getAllPersons, getAllUsers} from "../../util/APIUtils";

class Dashboard extends Component {

    constructor(props) {
        super(props);

        this.state = {
            usersList: [],
            personsList: []
        }
    }

    /* eslint-disable */
    static propTypes = {
        posts: PropTypes.any,
        isFetching: PropTypes.bool,
        dispatch: PropTypes.func.isRequired,
    };
    /* eslint-enable */

    static defaultProps = {
        posts: [],
        isFetching: false,
    };

    state = {
        isDropdownOpened: false
    };

    componentDidMount() {
        getAllUsers()
            .then(response => {
                this.setState({
                    usersList: response,
                });
            }).catch(error => {
            this.setState({
                loading: false
            });
        });

        getAllPersons()
            .then(response => {
                this.setState({
                    personsList: response,
                });

            }).catch(error => {
            this.setState({
                loading: false
            });
        });
    }

    renderRowUser(row) {
        return (<tr>
            <td>
                <img width={30}
                     className={cx('rounded-circle mr-sm text-center', s.adminPhoto)}
                     src={row.picturePath}
                     alt={row.name}/></td>
            <td>{row.email}</td>
            <td>
                {row.online ? (<span className="py-0 px-1 bg-success rounded text-white">online</span>)
                    : (<span className="py-0 px-1 bg-danger rounded text-white">offline</span>)}
            </td>
        </tr>)
    }

    renderRowPerson(row) {
        return (<tr>
            <td>{row.name}</td>
            <td>{row.cpf}</td>
            <td>{row.email}</td>
        </tr>)
    }

    render() {
        return (
            <div className={s.root}>
                <h1 className="mb-lg">Dashboard</h1>
                <Row>
                    <Col sm={12} md={6}>
                        <Widget
                            title={
                                <div>

                                    <h5 className="mt-0 mb-3">
                                        <i className="fa fa-user mr-xs opacity-70"/>{' '}
                                        Usuarios
                                    </h5>
                                </div>
                            }
                        >
                            <Table responsive borderless className={cx('mb-0', s.usersTable)}>
                                <thead>
                                <tr>
                                    <th>Foto</th>
                                    <th>Email</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>
                                {this.state.usersList.map(this.renderRowUser)}
                                </tbody>
                            </Table>
                        </Widget>
                    </Col>

                    <Col sm={12} md={6}>
                        <Widget
                            title={
                                <div>
                                    <h5 className="mt-0 mb-0">
                                        Pessoas recentes{' '}
                                        <Badge color="success" className="ml-xs">
                                            5
                                        </Badge>
                                    </h5>
                                    <p className="fs-sm mb-0 text-muted">
                                        Pessoas cadastrados por {JSON.parse(localStorage.getItem("currentUser")).name}
                                    </p>
                                </div>
                            }
                        >
                            <Table className="table-hover">
                                <thead>
                                <tr>

                                    <th>Nome</th>
                                    <th>Cpf</th>
                                    <th>Email</th>
                                </tr>
                                </thead>
                                {/* eslint-disable */}
                                <tbody>
                                {this.state.personsList.slice(0, 5).map(this.renderRowPerson)}
                                </tbody>
                                {/* eslint-enable */}
                            </Table>

                            <div className="d-flex justify-content-end">
                                <Link to="/app/persons" className="btn btn-default">
                                    Ver todas pessoas <Badge className="ml-xs"
                                                             color="danger">{this.state.personsList.length}</Badge>
                                </Link>
                            </div>
                        </Widget>
                    </Col>
                </Row>

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

export default connect(mapStateToProps)(Dashboard);
