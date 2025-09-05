import "./RecipeCover.css"

type Props = {
    name: string
}

export default function RecipeCover({ name }: Readonly<Props>) {
    return (
        <div className="cover">
            {name.charAt(0).toUpperCase()}
        </div>
    )
}
