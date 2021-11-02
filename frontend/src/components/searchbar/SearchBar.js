import './SearchBar.css'

const SearchBar = () => (
    <form className="temp" action="/" method="get">
        <label htmlFor="header-search">
            <span className="visually-hidden">Ad search</span>
        </label>
        <input
            type="text"
            id="header-search"
            placeholder="ad search"
            name="s"
        />
        <button className="search-button" type="submit">Search</button>
    </form>
);

export default SearchBar;