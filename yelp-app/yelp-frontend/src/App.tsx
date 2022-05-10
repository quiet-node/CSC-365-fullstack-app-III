import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Clusters from './pages/Clusters';
import Landing from './pages/Landing';
import YelpData from './pages/YelpData';
import WeightedGraph from './pages/WeightedGraph';

function App() {
  return (
    <Router>
      <Routes>
        <Route path='/' element={<Landing />} />
        <Route path='/yelp-data' element={<YelpData />} />
        <Route path='/clusters' element={<Clusters />} />
        <Route path='/graph' element={<WeightedGraph />} />
      </Routes>
    </Router>
  );
}

export default App;
