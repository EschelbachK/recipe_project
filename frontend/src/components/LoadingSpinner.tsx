import "./LoadingSpinner.css"

type LoadingSpinnerProps = {
    size?: number
    color?: string
}

export default function LoadingSpinner({ size = 40, color = "#3498db" }: Readonly<LoadingSpinnerProps>) {
    const style: React.CSSProperties = {
        width: size,
        height: size,
        borderColor: `${color} transparent transparent transparent`,
    }

    return (
        <div className="spinner" style={style}></div>
    )
}