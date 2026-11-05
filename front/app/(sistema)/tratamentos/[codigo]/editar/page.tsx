"use client";

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import type { Tratamento } from "@/app/types/tratamento";
import { buscarTratamentoPorId } from "@/app/services/tratamentoService";
import TratamentoForm from "@/app/(sistema)/tratamentos/components/TratamentoForm";

export default function EditarTratamento() {
    const parametro = useParams();
    const codigo = Number(parametro.codigo);
    const router = useRouter();
    const [dados, setDados] = useState<Tratamento | null>(null);

    useEffect(() => {
        buscarTratamentoPorId(codigo).then(setDados).catch(() => {
            alert("Registro não encontrado ou sem permissão!");
            router.push("/tratamentos");
        });
    }, [codigo, router]);

    if (!dados) return <p className="p-8 text-purple-800">Carregando dados...</p>;

    return (
        <div className="bg-purple-50 px-6 py-10">
            <div className="mx-auto max-w-4xl rounded-xl bg-white p-8">
                <Link href="/tratamentos" className="text-purple-700">Voltar para listagem</Link>
                <h1 className="my-6 text-3xl font-bold text-purple-800">Editar Tratamento {codigo}</h1>
                <TratamentoForm tratamentoExistente={dados} />
            </div>
        </div>
    );
}
