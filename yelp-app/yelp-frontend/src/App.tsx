import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Clusters from './pages/Clusters';
import Landing from './pages/Landing';
import YelpData from './pages/YelpData';

function App() {
  return (
    <Router>
      <Routes>
        <Route path='/' element={<Landing />} />
        <Route path='/yelp-data' element={<YelpData />} />
        <Route path='/clusters' element={<Clusters />} />
      </Routes>
    </Router>
  );
}

export default App;
