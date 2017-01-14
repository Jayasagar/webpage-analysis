
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

var AnalysisResult = React.createClass({

  render: function() {

    var hypermediaLinks = [];
    debugger;
    console.log("result:"+this.props.result);
    if (this.props.result.hypermediaLinks) {
        this.props.result.hypermediaLinks.INTERNAL.forEach(function(hypermedia) {
          hypermediaLinks.push(
            <Link link={hypermedia} key={hypermedia.link} domain={"Internal"}/>);
        });
        this.props.result.hypermediaLinks.EXTERNAL.forEach(function(hypermedia) {
              hypermediaLinks.push(
                <Link link={hypermedia} key={hypermedia.link} domain={"External"} />);
        });
    }
    return (
       <div>
            <h6>Page Title:{this.props.result.pageTitle}</h6>
            <h6>Html Version:{this.props.result.htmlVersion}</h6>
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
  }
});

var App = React.createClass({

  getResult: function() {
    var self = this;
    $.ajax({
        url: "http://localhost:8080/scraping/analyse?url=http://google.com",
      }).then(function(data) {
        console.log("data", data);
        self.setState({ result: data.data });
      });
  },

  getInitialState: function() {
    return { result: {} };
  },

  componentDidMount: function() {
    this.getResult();
  },

  render() {
    return ( <AnalysisResult result={this.state.result} /> );
  }
});

ReactDOM.render(<App />, document.getElementById('root') );
