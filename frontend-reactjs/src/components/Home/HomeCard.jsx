import { Link } from 'react-router-dom';

const HomeCard = ({ to, imgSrc, imgAlt = '', title, subtitle, }) => {

    return (
        <Link to={to}
              className="block focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-black rounded-md"
              aria-label={title}
              >
            <div className={`rounded-md overflow-hidden shadow hover:shadow-lg transition duration-300`}>
                <img
                    src={imgSrc}
                    alt={imgAlt}
                    className="w-full h-40 object-cover"
                    loading="lazy"
                />
                <div className = "p-4">
                    <h3 className="text-lg font-semibold">{title}</h3>
                    {subtitle && <p className="text-sm text-gray-500">{subtitle}</p>}
                </div>
            </div>
        </Link>
    );
};

export default HomeCard;
