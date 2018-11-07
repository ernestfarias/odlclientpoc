'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const follow = require('./follow');

console.log("efa->React version:" + React.version);

class App extends React.Component {



    constructor(props) {
        //ES6 initilizator mode
        console.log("did props");
        super(props);
        this.state = {blockingFlows: [], attributes: [], pageSize: 15, links: {}};
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
        this.updatePageSize = this.updatePageSize.bind(this);
        //why bindings , because when are called back and are not binded to in this case app.props
        //you will face it to be undefined when the function is actually called
    }


    componentDidMount_OLD_NO_PAGING() {
        //update state of react component Most basic
        //client get from server data and puts it into this.state.blockingFlows (client)
        console.log("did the mount!!!");
//        alert("CompDidMount")
        debugger;
        client({
            method: 'GET',
            path: '/api/blockingFlows'}).done();
        client({
            method: 'GET',
            path: '/api/blockingFlows'}).done(response => {
           this.setState({blockingFlows: response.entity._embedded.blockingFlows})
        });
    }

    onNavigate(navUri) {
        client({method: 'GET', path: navUri}).done(blockingflowsCollection => {
            this.setState({
                blockingFlows: blockingflowsCollection.entity._embedded.blockingFlows,
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                links: blockingflowsCollection.entity._links
            });
        });
    }

    onDelete(blockingFlow) {
        debugger;
        client({method: 'DELETE', path: blockingFlow._links.self.href}).done(response => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    componentDidMount() {
        //update state of react component
        this.loadFromServer(this.state.pageSize);
    }

    // tag::update-page-size[]
    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
    }
    // end::update-page-size[]

    loadFromServer(pageSize) {
        //PAGING
  //      debugger;
        var root = '/api';
        follow(client, root, [
            {rel: 'blockingFlows', params: {size: pageSize}}]
        ).then(blockingflowsCollection => {
            return client({
                method: 'GET',
                path: blockingflowsCollection.entity._links.profile.href,
                headers: {'Accept': 'application/schema+json'}
            }).then(schema => {
                this.schema = schema.entity;
                return blockingflowsCollection;
            });
        }).done(blockingflowsCollection => {
            this.setState({
                blockingFlows: blockingflowsCollection.entity._embedded.blockingFlows,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: blockingflowsCollection.entity._links});
        });
    }

    render() {
        //console.log("render:efalocodebug13");
        //alert("1");
        //console.log(this.state.blockingFlows);
        //console.log(this.props.blockingFlows);
        //debugger;
               return (
                  <BlockingFlowList pblockingflowlist={this.state.blockingFlows}
                                    links={this.state.links}
                                    pageSize={this.state.pageSize}
                                    onNavigate={this.onNavigate}
                                    onDelete={this.onDelete}
                                    updatePageSize={this.updatePageSize}
                  />)
       // debugger;

    }
}
// end off app

class BlockingFlowList extends React.Component{
    constructor(props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    // tag::handle-page-size-updates[]
    handleInput(e) {
        e.preventDefault();
        var pageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.updatePageSize(pageSize);
        } else {
            ReactDOM.findDOMNode(this.refs.pageSize).value =
                pageSize.substring(0, pageSize.length - 1);
        }
    }
    // end::handle-page-size-updates[]

    // tag::handle-nav[]
    handleNavFirst(e){
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }
    // end::handle-nav[]

    render() {
       // debugger;
        //esto mapea this.props.pblockingflowlist adentro de mappedblockingFlow
        //fks key contains the pblockingflowlist[X]._links.self.href of each pblockingflowlist in the list
        var blockfs = this.props.pblockingflowlist.map(mappedblockingFlow =>
       <BlockingFlow key={mappedblockingFlow._links.self.href} pblockingflow={mappedblockingFlow} onDelete={this.props.onDelete}/>);


//        if(this.props.links !== undefined) {
                //if links are empty
        var navLinks = [];
        if ("first" in this.props.links) {
            navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ("prev" in this.props.links) {
            navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ("next" in this.props.links) {
            navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ("last" in this.props.links) {
            navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        //}

        return (
            <div>

                <table>
                <tbody>
                <tr>
                    <th>Flow Name</th>
                    <th>IP</th>
                    <th>Priority</th>
                    <th>Action</th>
                </tr>
                {blockfs}
                </tbody>
            </table>
                <div>
                    {/*navigation buttons*/}
                    {navLinks}
                    {/*Items per page*/}
                    {<input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput}/>}
                </div>
            </div>
        )
    }
}

class BlockingFlow extends React.Component{

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.pblockingflow);
    }


    render() {
        return (
            <tr>
                <td>{this.props.pblockingflow.flowname}</td>
                <td>{this.props.pblockingflow.ip_destination_match}</td>
                <td>{this.props.pblockingflow.priority}</td>
                <td>
                    <button onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)
//






