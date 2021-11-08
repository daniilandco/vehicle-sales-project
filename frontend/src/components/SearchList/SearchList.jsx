import React, {useEffect} from 'react';
import './SearchListStyle.css';
import Ad from '../Ad/Ad';
import {useSearch} from '../../providers/SearchProvider';

const SearchList = () => {
  const search = useSearch();

  useEffect(() => {
    async function fetchData() {
      await search.onSearch();
    }

    fetchData();
  }, [search.terms]);

  useEffect(() => {
    console.log('list rendered');
  });

  return (
      <div id="list">
        {search.ads?.map((ad) => (
            <Ad key={ad.id} ad={ad}/>
        ))}
      </div>
  );
};

export default SearchList;
