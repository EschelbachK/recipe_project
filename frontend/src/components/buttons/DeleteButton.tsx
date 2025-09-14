import * as React from "react";
import "./Button.css"

type Props = {
    onClick: (e: React.MouseEvent) => void;
};

export default function DeleteButton({onClick}: Readonly<Props>) {
    return (
        <button
            className="action-btn"
            onClick={onClick}
        >
            🗑️
        </button>
    );
}
