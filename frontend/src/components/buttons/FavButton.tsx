import * as React from "react";
import "./Button.css"

type Props = {
    onClick: (e: React.MouseEvent) => void;
};

export default function FavButton({onClick}: Readonly<Props>) {
    return (
        <button
            className="action-btn fav"
            onClick={onClick}
        >
            Fav❤️
        </button>
    );
}
