import * as React from "react"
import "./Button.css"

type Props = {
    isFav: boolean
    onClick: (e: React.MouseEvent) => void
}

export default function FavButton({ isFav, onClick }: Readonly<Props>) {
    return (
        <button
            className={`action-btn fav ${isFav ? "active" : ""}`}
            onClick={onClick}
        >
            <span>♥</span>
        </button>
    )
}
