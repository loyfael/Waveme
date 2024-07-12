import largeBanner from "../../assets/banner-large.webp"
import smallBanner from "../../assets/banner-small.webp"
import './Banner.css'

const Banner = () => {
    return (
        <>
            <main>
                <picture>
                    <source srcSet={largeBanner} media="(min-width: 768px)" />
                    <img src={smallBanner} alt="Banner" />
                </picture>
            </main>
        </>
    )
}

export default Banner