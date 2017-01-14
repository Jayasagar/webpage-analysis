
var Link = React.createClass({

  getInitialState: function() {
    return {display: true };
  },

  render: function() {
    if (this.state.display==false) return null;
    else return (
      <tr>
          <td>{this.props.link.link}</td>
          <td>{this.props.domain}</td>
          <td>{this.props.link.type}</td>
          <td>{this.props.link.text}</td>
      </tr>
    );
  }
});

var HeadingLevel = React.createClass({

  getInitialState: function() {
    return {display: true };
  },

  render: function() {
    if (this.state.display==false) return null;
    else return (
      <tr>
          <td>{this.props.level}</td>
          <td>{this.props.count}</td>
      </tr>
    );
  }
});

var AnalysisResult = React.createClass({

  render: function() {

    var hypermediaLinks = [];
    var headingGroups = [];
    debugger;
    if (this.props.status && this.props.result.hypermediaLinks) {
        if (this.props.result.hypermediaLinks.INTERNAL) {
            this.props.result.hypermediaLinks.INTERNAL.forEach(function(hypermedia) {
              hypermediaLinks.push(
                <Link link={hypermedia} key={hypermedia.link} domain={"Internal"}/>);
            });
        }
        if (this.props.result.hypermediaLinks.EXTERNAL) {
            this.props.result.hypermediaLinks.EXTERNAL.forEach(function(hypermedia) {
                  hypermediaLinks.push(
                    <Link link={hypermedia} key={hypermedia.link} domain={"External"} />);
            });
        }
    }
    if (this.props.status && this.props.result.headings) {
        headingGroups.push(<HeadingLevel level={"h1"} key={"h1"} count={this.props.result.headings.h1} />);
        headingGroups.push(<HeadingLevel level={"h2"} key={"h2"} count={this.props.result.headings.h2} />);
        headingGroups.push(<HeadingLevel level={"h3"} key={"h3"} count={this.props.result.headings.h3} />);
        headingGroups.push(<HeadingLevel level={"h4"} key={"h4"} count={this.props.result.headings.h4} />);
        headingGroups.push(<HeadingLevel level={"h5"} key={"h5"} count={this.props.result.headings.h5} />);
        headingGroups.push(<HeadingLevel level={"h6"} key={"h6"} count={this.props.result.headings.h6} />);
    }
    if (this.props.status) {
        return (
           <div>
                <h6>Page Title: {this.props.result.pageTitle}</h6>
                <h6>Html Version: {this.props.result.htmlVersion}</h6>
                <h6>Is this Login form: {'' + this.props.result.loginForm}</h6>
                <table className="table table-striped">
                  <thead>
                      <tr>
                          <th>Heading Level</th>
                          <th>Number of times used</th>
                      </tr>
                  </thead>
                  <tbody>{headingGroups}</tbody>
                </table>
                <table className="table table-striped">
                  <thead>
                      <tr>
                          <th>Url</th>
                          <th>Domain</th>
                          <th>Type</th>
                          <th>Description</th>
                      </tr>
                  </thead>
                  <tbody>{hypermediaLinks}</tbody>
                </table>
          </div>
        );
     } else {
       return (
                <div>
                    <span id="inputError">{this.props.message} </span>
                </div>
       );
     }
  }
});

var App = React.createClass({

  getAnalysisResult: function() {
    var self = this;
    self.setState({result: {}});
    $.ajax({
        url: "http://localhost:8080/scraping/analyse?url="+this.state.url,
      }).then(function(result) {
        debugger;
        self.setState({status: result.status});
        if (result.status) {
            self.setState({result: result.data});
        } else {
            self.setState({message: result.message});
        }
      });
  },

  updateUrl: function(event) {
    this.setState({url:event.target.value});
  },

  getInitialState: function() {
    return {result: {}, url:"", status:false, message:""};
  },

  render() {
    return (
    <div>
        <h3> Simple web page analysis report. Try out!</h3>
        <input class="form-control" type="text"
            onChange={this.updateUrl.bind(this)}
            placeholder="Enter a valid url..."/>

        <button className="btn btn-info" onClick={this.getAnalysisResult}>Submit</button>

        <AnalysisResult result={this.state.result} message={this.state.message} status={this.state.status}/>
    </div>
    );
  }
});

ReactDOM.render(<App />, document.getElementById('root') );
