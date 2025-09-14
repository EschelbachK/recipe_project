type ShoppingButtonProps = {
    isActive?: boolean
    onClick: (e: React.MouseEvent<HTMLButtonElement>) => void
}

export default function ShoppingButton({isActive = false, onClick}: Readonly<ShoppingButtonProps>) {
    return (
        <button
            type="button"
            className={`action-btn shopping ${isActive ? "active" : ""}`}
            onClick={onClick}
        >
            {isActive ? "✔" : "🛒"}
        </button>
    )
}