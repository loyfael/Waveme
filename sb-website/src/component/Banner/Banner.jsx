import largeBanner from "../../assets/banner-large.webp"
import smallBanner from "../../assets/banner-small.png"
import './Banner.css'

const Banner = () => {
    return (
        <>
            <main>
                <picture>
                    <source srcSet={largeBanner} media="(min-width: 768px)" />
                    <img src={smallBanner} alt="Banner" />
                </picture>
                <h1>Press to impress, sound like a mess!</h1>
            </main>
        </>
    )
}

export default Banner