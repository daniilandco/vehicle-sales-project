import React, {useEffect, useState} from 'react';
import './AdImage.css';
import placeholder from '../../assets/placeholder.png';
import DataService from '../../services/DataService';
import loader from '../../assets/loader.svg';

const AdImage = ({id}) => {
  const [isLoading, setLoading] = useState(true);
  const [image, setImage] = useState(placeholder);

  useEffect(() => {
    async function fetchData() {
      const src = await DataService.getMainAdPhoto(id);
      setImage((await src) ? src : placeholder);
      setLoading(false);
    }

    fetchData();
  }, [id]);

  return <img src={isLoading ? loader : image} alt="loading error" className="ad__img"/>;
};

export default AdImage;
