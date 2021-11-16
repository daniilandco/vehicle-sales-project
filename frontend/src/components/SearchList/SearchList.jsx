import React from 'react';
import './SearchListStyle.css';
import Ad from '../Ad/Ad';
import {useSearch} from '../../providers/SearchProvider';
import loader from '../../assets/loader.svg';

const SearchList = () => {
  const search = useSearch();

  return (
      <div className="list">
          {search.loading ? <img src={loader} alt="error"/> : search.ads?.map((ad) => (
              <Ad key={ad.id} ad={ad}/>
          ))}
      </div>
  );
};

export default SearchList;
